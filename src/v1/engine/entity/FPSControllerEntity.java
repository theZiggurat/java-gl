package v1.engine.entity;

import v1.engine.draw.Camera;
import v1.engine.enviorment.Scene;
import v2.engine.system.InputCore;
import v1.engine.util.State;
import v1.engine.util.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class FPSControllerEntity extends CameraEntity{

    private float MAX_SPEED = 1;
    Entity heldItem;

    private PlayerContext context = PlayerContext.GROUND;

    private final Vector3f DEF_GRAVITY = new Vector3f(0, 0, 0);
    private float walkingMultiplier = .05f;

    private Vector3f vel, accel;

    private float playerHeight = 2f;

    private float dropMultiplier;
    private float sprintMultiplier;

    private Scene scene;

    public FPSControllerEntity(Camera camera){
        this.camera = camera;
        vel = new Vector3f();
        accel = new Vector3f(0,-9.81f, 0);
    }

    public FPSControllerEntity(){
        this.camera = new Camera();
        vel = new Vector3f();
        accel = new Vector3f(0,-9.81f, 0);
        sprintMultiplier = 2;
        dropMultiplier = 2;
    }

    @Override
    public void handle(Window window){

        handleMouse();
        handleSprint(window);

        Vector3f acceleration = new Vector3f(DEF_GRAVITY);
        Vector3f velocity = new Vector3f(vel);

        if(context == PlayerContext.AIR){
            vel.add(acceleration.mul((float) State.getInstance().getInterval()*dropMultiplier));
        }

        handlePosition(getPos(), window);
        handleJump(vel, window);
        getPos().add(velocity.mul((float) State.getInstance().getInterval() * sprintMultiplier));

        float height;
        // terrain collision
        if(scene.getTerrain() == null){
            height = 0;
        } else {
            height =  scene.getTerrain().getHeight(getPos(), playerHeight);
        }

        if(height >= getPos().y){
            getPos().y = height;
            vel.y = 0;
            context = PlayerContext.GROUND;
        } else {
            context = PlayerContext.AIR;
        }

        Vector3f cameraCoord = new Vector3f(getPos());
        cameraCoord.y+=0;

        camera.setPosition(cameraCoord);


        if(heldItem != null){
            heldItem.setPos(new Vector3f(getPos()).add(getLookNorm()));
        }

    }

    private void handleMouse(){
        InputCore input = InputCore.getInstance();

        Vector2f rotation = new Vector2f((float)input.getDisplacement().x, (float)input.getDisplacement().y);
        getCamera().moveRotation(rotation.x * MOUSE_SENS,
                rotation.y * MOUSE_SENS,
                0);
    }

    private void handlePosition(Vector3f pos, Window window){

        Vector3f cameraHandle = new Vector3f(0,0,0);
        if(window.isKeyPressed(GLFW_KEY_W)){
            cameraHandle.z = -1;
        } else if(window.isKeyPressed(GLFW_KEY_S)) {
            cameraHandle.z = 1;
        }if(window.isKeyPressed(GLFW_KEY_A)){
            cameraHandle.x = -1;
        } else if(window.isKeyPressed(GLFW_KEY_D)) {
            cameraHandle.x = 1;
        }if(window.isKeyPressed(GLFW_KEY_UP)){
            cameraHandle.y = -.1f;
        } else if(window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraHandle.y = .1f;
        }
        if(cameraHandle.z!=0){
            pos.x += walkingMultiplier*(float) Math.sin(Math.toRadians(camera.getRotation().y))*-1*cameraHandle.z;
            pos.z += walkingMultiplier*(float) Math.cos(Math.toRadians(camera.getRotation().y))*cameraHandle.z;
        }
        if(cameraHandle.x!=0){
            pos.x += walkingMultiplier*(float) Math.sin(Math.toRadians(camera.getRotation().y-90))*-1*cameraHandle.x;
            pos.z += walkingMultiplier*(float) Math.cos(Math.toRadians(camera.getRotation().y-90))*cameraHandle.x;
        }
        pos.y+=cameraHandle.y;
    }

    private void handleJump(Vector3f vel, Window window){
        if(window.isKeyPressed(GLFW_KEY_SPACE)&&context==PlayerContext.GROUND){
            vel.y += 5;
            getPos().y+=.1;
        }
    }

    private void handleSprint(Window window){
        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            sprintMultiplier = 2;
            dropMultiplier = 2;
        }
        else{
            sprintMultiplier = 1;
            dropMultiplier = 1;
        }
    }


    private Vector3f maxSpeed(Vector3f speed){
        if(speed.length()>MAX_SPEED){
            return speed.normalize().mul(MAX_SPEED);
        }
        return speed;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vector3f getLookNorm(){
        Vector3f ret = new Vector3f(0,0,1);
        //ret.rotateX((float)Math.toRadians(camera.getRotation().x));
        ret.rotateY((float)Math.toRadians(camera.getRotation().y-180));
        return ret;
    }

    public Vector3f getVel() {
        return vel;
    }

    public void setVel(Vector3f vel) {
        this.vel = vel;
    }

    public Vector3f getAccel() {
        return accel;
    }

    public void setAccel(Vector3f accel) {
        this.accel = accel;
    }

    @Override
    public void debug() {
        System.out.println("PlayerRot: "    + getLookNorm().x + " "
                                            + getLookNorm().y + " "
                                            + getLookNorm().z);
        System.out.println("PlayerPos: "    + camera.getPosition().x + " "
                                            + camera.getPosition().y + " "
                                            + camera.getPosition().z);
    }

    public Entity getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Entity heldItem) {
        this.heldItem = heldItem;
    }

    public Scene getScene(){
        return scene;
    }

    public void setScene(Scene _scene){
        this.scene = _scene;
    }

    private enum PlayerContext{
        GROUND,
        AIR
    }


}
