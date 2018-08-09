package v1.engine.entity;

import v1.engine.draw.mesh.Mesh3D;
import v2.engine.system.InputCore;
import v1.engine.util.Window;
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
        InputCore mouse = InputCore.getInstance();

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
