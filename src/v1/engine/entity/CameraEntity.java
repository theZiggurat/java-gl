package v1.engine.entity;

import v1.engine.draw.Camera;
import v1.engine.draw.mesh.Mesh3D;
import org.joml.Vector3f;

public abstract class CameraEntity extends StaticEntity implements CVXController{

    public static float MOUSE_SENS = 0.15f;

    Camera camera;

    public CameraEntity(){
        super();
        camera = new Camera(new Vector3f(0,0,0),new Vector3f(0,0,0));
    }

    public CameraEntity(Mesh3D mesh3D){
        super(mesh3D);
        camera = new Camera(new Vector3f(0,0,0),new Vector3f(0,0,0));
    }

    public Camera getCamera(){
        return camera;
    }

    public void setCamera(Camera _camera){
        this.camera = _camera;
    }

    public static float getMouseSens(){
        return MOUSE_SENS;
    }

    public static void setMouseSens(float _MOUSE_SENS){
        MOUSE_SENS = _MOUSE_SENS;
    }

}
