package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.*;
import v2.engine.application.layout.Box;
import v2.engine.application.element.Element;
import v2.engine.application.event.InputManager;
import v2.engine.application.event.KeyboardEvent;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.scene.node.Node;
import v2.engine.scene.node.Transform;
import v2.engine.system.Window;
import v2.engine.utils.Interpolation;

import java.lang.Math;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends Element {

    // TODO: redo as abstract class

    @Getter @Setter Function<Integer, Float> speedMod = (
                 e -> ( (float) Math.exp(-e) ) * 0.5f
            );

    @Setter @Getter private double FOV = 60;
    @Getter @Setter private int speedLevel = 4;
    @Getter private final float ZNEAR = .01f;
    @Getter private final float ZFAR = 10000;

    private final float DEF_MOUSE_SENS = 0.2f;
    @Setter @Getter private float mouseSens = DEF_MOUSE_SENS;

    private Vector3f velocity;
    float speedLimit = 10;

    private SceneContext context;

    public Transform transform;

    public Vector3f delta_rotate;

    public Camera(SceneContext context){

        super();
        this.context = context;
        this.transform = new Transform();
        transform.setTranslation(new Vector3f(0,0,-10));
        transform.setRotation(new Vector3f(0,0,180));
        velocity = new Vector3f();
        delta_rotate = new Vector3f();

        onEvent(e -> {

            if(e instanceof KeyboardEvent){
                KeyboardEvent k = (KeyboardEvent) e;
                if(k.getAction() == KeyboardEvent.KEY_PRESSED){
                    if(k.getKey() == GLFW_KEY_MINUS) speedLevel--;
                    if(k.getKey() == GLFW_KEY_EQUAL) speedLevel++;
                }
            }

            else if(e instanceof MouseClickEvent){
                MouseClickEvent m = (MouseClickEvent)e;

                Window window = Window.instance();

                if(m.getAction() == MouseClickEvent.BUTTON_CLICK){

                    if(m.getKey() == GLFW_MOUSE_BUTTON_1){

                        Box sceneBox = context.getParent().getAbsoluteBox();
                        Vector2f pos = sceneBox.within(m.getScreenPos());
                        Vector2i resolution = sceneBox.resolution();

                        Vector2i point = new Vector2i((int)(pos.x*resolution.x), (int)((1-pos.y)*resolution.y));
                        Node selected = context.getPicking().pick(point.x, point.y);

                        if(selected != null) {
                            if(selected.isSelected())
                                if((m.getMods() & GLFW_MOD_CONTROL)==0)
                                    context.getSelectionManager().clear();
                                else context.getSelectionManager().remove(selected);
                            else {
                                if((m.getMods() & GLFW_MOD_CONTROL)==0)
                                    context.getSelectionManager().clear();
                                context.getSelectionManager().addSelection(selected);
                            }
                        }
                    }

                    // CAMERA ROTATE START
                    if(m.getKey() == GLFW_MOUSE_BUTTON_2 || m.getKey() == GLFW_MOUSE_BUTTON_3){
                        window.hideCursor(true);
                        window.lockCursor(true);
                        window.lockCursor(false);
                    }

                }

                if(m.getAction() == MouseClickEvent.BUTTON_HELD){

                    //  CAMERA ROTATE HOLD
                    if(m.getKey() == GLFW_MOUSE_BUTTON_2){
                        delta_rotate.x = m.getPixelScreenDelta().y * mouseSens;
                        delta_rotate.y = m.getPixelScreenDelta().x *mouseSens;
                    }

                    // CAMERA PAN
                    if(m.getKey() == GLFW_MOUSE_BUTTON_3){
                        velocity.add(getUp().mul(m.getPixelScreenDelta().y * 0.05f * speedMod.apply(speedLevel-2)));
                        velocity.add(getRight().mul(m.getPixelScreenDelta().x * -0.05f * speedMod.apply(speedLevel-2)));
                    }

                }

                if(m.getAction() == MouseClickEvent.BUTTON_RELEASED){

                    // CAMERA ROTATE END
                    if(m.getKey() == GLFW_MOUSE_BUTTON_2|| m.getKey() == GLFW_MOUSE_BUTTON_3){
                        if(window.isHidden()){
                            window.hideCursor(false);
                            window.setCursorPos(window.getLockedcursorPos());
                        }
                    }

                }
            }
        });
    }

    float t = 0;

    public void update(){

        // polling for movement instead of getting keyboard info from event
        // since GLFW hold mode has too long of a delay
        // could make a pre-hold event type but this solution is easier since the code is there already
        if(InputManager.instance().isKeyHeld(GLFW_KEY_W)) velocity.add(getForward().mul(speedMod.apply(speedLevel)));
        if(InputManager.instance().isKeyHeld(GLFW_KEY_S)) velocity.add(getForward().mul(-speedMod.apply(speedLevel)));
        if(InputManager.instance().isKeyHeld(GLFW_KEY_A)) velocity.add(getRight().mul(speedMod.apply(speedLevel)));
        if(InputManager.instance().isKeyHeld(GLFW_KEY_D)) velocity.add(getRight().mul(-speedMod.apply(speedLevel)));

        // the zoom function
        // TODO: Make update based math lib
        if(InputManager.instance().isKeyHeld(GLFW_KEY_Q)) t += 0.01f;
        else t -= 0.01f;
        if(t < 0) t = 0;
        if(t > 1) t = 1;

        // trim velocity
        if(velocity.length() > speedLimit)
            velocity.normalize().mul(speedLimit);

        // translate by velocity
        transform.translate(velocity);



        FOV = (double) Interpolation.smootherstep(60, 25, t);
        mouseSens = DEF_MOUSE_SENS/(1+t*2);

        velocity.mul(.95f);

        transform.rotate(delta_rotate.x, delta_rotate.y, 0);
        delta_rotate.mul(0.6f);

    }

    public Matrix4f getViewProjectionMatrix(){
        return getProjectionMatrix().mul(getViewMatrix());
    }

    public Matrix4f getProjectionMatrix() {
        float apectRatio = context.getAspectRatio();
        Matrix4f ret = new Matrix4f();
        ret.identity();
        ret.perspective((float)Math.toRadians(FOV), apectRatio, ZNEAR, ZFAR);
        return ret;
    }

    public Matrix4f getOrthoProjectionMatrix() {
        Matrix4f ret = new Matrix4f();
        ret.identity();
        ret.ortho(-20, 20, -20, 20, 0.0001f, 100f);
        return ret;
    }

    public Matrix4f getProjectionMatrixLH() {
        float apectRatio = context.getAspectRatio();
        Matrix4f ret = new Matrix4f();
        ret.identity();
        ret.perspectiveLH((float)Math.toRadians(FOV), apectRatio, ZNEAR, ZFAR);
        return ret;
    }

    public Matrix4f getViewMatrix(){
        Matrix4f ret = new Matrix4f();
        ret.identity();
        Vector3f rot = transform.getRotation();
        ret.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(rot.z), new Vector3f(0,0,1));

        ret.translate(transform.getTranslation().mul(-1));
        return ret;
    }

    public Vector3f getForward(){
        Matrix4f viewMatrix = getViewMatrix();
        Vector3f ret = new Vector3f();
        ret.x = -viewMatrix.m02();
        ret.y = -viewMatrix.m12();
        ret.z = -viewMatrix.m22();
        return ret;
    }

    public Vector3f getUp(){
        Matrix4f viewMatrix = getViewMatrix();
        Vector3f ret = new Vector3f();
        ret.x = -viewMatrix.m01();
        ret.y = -viewMatrix.m11();
        ret.z = -viewMatrix.m21();
        return ret;
    }

    public Vector3f getRight(){
        Matrix4f viewMatrix = getViewMatrix();
        Vector3f ret = new Vector3f();
        ret.x = -viewMatrix.m00();
        ret.y = -viewMatrix.m10();
        ret.z = -viewMatrix.m20();
        return ret;
    }

}
