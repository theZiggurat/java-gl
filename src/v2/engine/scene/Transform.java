package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    @Getter @Setter private Vector3f translation;
    @Getter @Setter private Vector3f rotation;
    @Getter @Setter private Vector3f scaling;

    public Transform(){
        translation = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scaling = new Vector3f(1,1,1);
    }

    public Matrix4f getModelMatrix(){
        Matrix4f ret = new Matrix4f();
        ret.identity().translate(getTranslation())
                .rotateX((float)Math.toRadians(-rotation.x))
                .rotateY((float)Math.toRadians(-rotation.y))
                .rotateZ((float)Math.toRadians(-rotation.z))
                .scale(getScaling());
        return ret;
    }
}
