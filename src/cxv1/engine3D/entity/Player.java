package cxv1.engine3D.entity;

import cxv1.engine3D.draw.Camera;
import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.util.State;
import cxv1.engine3D.util.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Player extends StaticEntity {

    private float MAX_SPEED = 1;

    private PlayerContext context = PlayerContext.GROUND;

    private Camera camera;

    private final Vector3f DEF_GRAVITY = new Vector3f(0, -9.81f, 0);
    private float walkingMultiplier = .05f;

    private Vector3f pos, vel, accel;

    float yaw;
    boolean leftYaw = false, rightYaw = false;
    private final float MAX_YAW = 60;
    private final float YAW_RATE = 0.3f;

    public Player(Camera camera){
        this.camera = camera;
        pos = new Vector3f();
        vel = new Vector3f();
        accel = new Vector3f(0,-9.81f, 0);
    }

    public Player(){
        this.camera = new Camera();
        pos = new Vector3f(0,100,0);
        vel = new Vector3f();
        accel = new Vector3f(0,-9.81f, 0);
        yaw = 0.0f;
    }

    public void update(Terrain terrain, State state, Window window){

        Vector3f acceleration = new Vector3f(DEF_GRAVITY);
        Vector3f velocity = new Vector3f(vel);

        if(context == PlayerContext.AIR){
            vel.add(acceleration.mul((float) state.getInterval()));
        }

        handlePosition(pos, window);
        handleJump(vel, window);
        pos.add(velocity.mul((float) state.getInterval()));


        // terrain collision
        float height = terrain.getHeight(pos);
        if(height >= pos.y){
            pos.y = height;
            vel.y = 0;
            context = PlayerContext.GROUND;
        } else {
            context = PlayerContext.AIR;
        }

        Vector3f cameraCoord = new Vector3f(pos);
        cameraCoord.y+=0;

        camera.setPosition(cameraCoord);

        handleYaw(window);

        if(leftYaw&&rightYaw){

        } else if(leftYaw){
            if(-yaw>=MAX_YAW){
                yaw = -MAX_YAW;
            }
            else{
                yaw-=YAW_RATE;
            }
        } else if(rightYaw){
            if(yaw>=MAX_YAW){
                yaw = MAX_YAW;
            }
            else{
                yaw+=YAW_RATE;
            }
        }
        else{
            if(yaw>0){
                yaw-=YAW_RATE;
            }
            if(yaw<0){
                yaw+=YAW_RATE;
            }
        }

        camera.getRotation().z = yaw;

    }

    private void handleYaw(Window window){
        if(window.isKeyPressed(GLFW_KEY_Q)){
            leftYaw = true;
        } else{
            leftYaw = false;
        }
        if(window.isKeyPressed(GLFW_KEY_E)){
            rightYaw = true;
        } else {
            rightYaw = false;
        }
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
            cameraHandle.y = -1;
        } else if(window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraHandle.y = 1;
        }
        if(cameraHandle.z!=0){
            pos.x += walkingMultiplier*(float) Math.sin(Math.toRadians(camera.getRotation().y))*-1*cameraHandle.z;
            pos.z += walkingMultiplier*(float) Math.cos(Math.toRadians(camera.getRotation().y))*cameraHandle.z;
        }
        if(cameraHandle.x!=0){
            pos.x += walkingMultiplier*(float) Math.sin(Math.toRadians(camera.getRotation().y-90))*-1*cameraHandle.x;
            pos.z += walkingMultiplier*(float) Math.cos(Math.toRadians(camera.getRotation().y-90))*cameraHandle.x;
        }
    }

    private void handleJump(Vector3f vel, Window window){
        if(window.isKeyPressed(GLFW_KEY_SPACE)&&context==PlayerContext.GROUND){
            vel.y = 5;
            pos.y+=.1;
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

    @Override
    public Vector3f getPos() {
        return pos;
    }

    @Override
    public void setPos(Vector3f pos) {
        this.pos = pos;
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

    public void debug(){
        System.out.println("PlayerCoord: "+getPos().toString()+getVel().toString()+getAccel().toString());
    }

    private enum PlayerContext{
        GROUND,
        AIR
    }


}
