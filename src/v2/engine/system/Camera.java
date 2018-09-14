package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import v2.engine.scene.Transform;

import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends Transform<Camera> {

    private final Vector3f ABS_UP = new Vector3f(0,1,0);

    @Getter @Setter Function<Integer, Float> speedMod = (
                 e -> ( (float) Math.exp(-e) ) * 0.5f
            );

    @Setter @Getter private double FOV;
    @Getter @Setter private int speedLevel = 0;
    @Getter private final float ZNEAR = .01f;
    @Getter private final float ZFAR = 10000;
    @Setter @Getter private float mouseSens = 0.3f;


    public Camera(){
        super();
        setTranslation(new Vector3f(0,0,-10));
        setRotation(new Vector3f(0,180,0));
        FOV = 85;
    }

    public Camera(Vector3f position){
        this();
        translateTo(position);
    }

    public void update(){
        InputCore input = InputCore.instance();

        if(input.isButtonHeld(1)){ // right click
            rotate((float)input.getDisplacement().y * mouseSens,
                    (float)input.getDisplacement().x * mouseSens,  0);
        }

        if(input.isButtonHeld(2)){ // middle click
            translate(getUp().mul((float)input.getDisplacement().y * .05f * speedMod.apply(speedLevel-2)));
            translate(getRight().mul((float)input.getDisplacement().x * -.05f * speedMod.apply(speedLevel-2)));
        }

        if(input.isButtonPressed(0)) { // left click
            float ssx = (float) (2 * (input.getCursorPos().x/ Window.instance().getWidth()) - 1);
            float ssy = (float) (1 - (2 * (input.getCursorPos().y/ Window.instance().getHeight())));
        }

        if(input.isKeyPressed(GLFW_KEY_MINUS)){
            speedLevel--;
        } else if (input.isKeyPressed(GLFW_KEY_EQUAL)){
            speedLevel++;
        }

        FOV += input.getScrollAmount();

        if(input.isKeyHeld(GLFW_KEY_W)){
            translate(getForward().mul(speedMod.apply(speedLevel)));
        }
        if(input.isKeyHeld(GLFW_KEY_S)){
            translate(getForward().mul(-speedMod.apply(speedLevel)));
        }
        if(input.isKeyHeld(GLFW_KEY_A)){
            translate(getRight().mul(speedMod.apply(speedLevel)));
        }
        if(input.isKeyHeld(GLFW_KEY_D)){
            translate(getRight().mul(-speedMod.apply(speedLevel)));
        }


    }

    public Matrix4f getProjectionMatrix() {
        float apectRatio = (float) Window.instance().getWidth() /
                (float) Window.instance().getHeight();
        Matrix4f ret = new Matrix4f();
        ret.identity();
        ret.perspective((float)Math.toRadians(FOV), apectRatio, ZNEAR, ZFAR);
        return ret;
    }

    public Matrix4f getViewMatrix(){
        Matrix4f ret = new Matrix4f();
        ret.identity();
        ret.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(rotation.z), new Vector3f(0,0,1));

        ret.translate(getTranslation().mul(-1));
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
