package v1.engine.draw;

import org.joml.Vector3f;

public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;

    public Camera(){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    public Camera(Vector3f position, Vector3f rotation){
        this.position = position;
        this.rotation = rotation;
    }

    public void movePosition(float dx, float dy, float dz){
        if(dz!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y))*-1*dz;
            position.z += (float) Math.cos(Math.toRadians(rotation.y))*dz;
        }
        if(dx!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y-90))*-1*dx;
            position.z += (float) Math.cos(Math.toRadians(rotation.y-90))*dx;
        }
        position.y+=dy;
    }

    public void moveRotation(float dx, float dy, float dz){
        rotation.x += dx;
        rotation.y += dy;
        rotation.z += dz;
    }

    public Vector3f getPosition(){return position;}
    public Vector3f getRotation(){return rotation;}
    public void setPosition(Vector3f position){
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }
    public void setRotation(Vector3f rotation){
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;
    }
}
