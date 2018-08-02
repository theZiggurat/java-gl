package cxv1.engine3D.entity;

import cxv1.engine3D.draw.mesh.Mesh3D;
import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.util.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PanControllerEntity extends CameraEntity{



    public PanControllerEntity(){
        super();
    }

    public PanControllerEntity(Mesh3D mesh3D){
        super(mesh3D);
    }


    @Override
    public void handle(Window window) {

        handleMouse();

    }

    private void handleMouse(){
        MouseInput mouse = MouseInput.getInstance();

        if(mouse.isRightPressed()){

            Vector2f displacement = mouse.getDisplacement();
            getCamera().moveRotation(displacement.x * MOUSE_SENS,
                    displacement.y * MOUSE_SENS, 0);
            setRot(new Vector3f(getCamera().getRotation().x,
                                getCamera().getRotation().y,
                                getCamera().getRotation().z));
        }
    }
}
