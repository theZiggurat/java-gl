package engine.scene.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DirectionalLight extends Light {

    @Getter @Setter
    private Vector3f ambientLight;

    @Getter private Matrix4f lightProjection;
    @Getter private Matrix4f lightView;

    private float size = 40f;

    public DirectionalLight(){
        super();
        lightProjection = new Matrix4f().ortho(
                -size, size, -size, size, -size, size
        );
        lightView = new Matrix4f().lookAt(
                0f, 5f, -5f,
                0,0,0,
                0,1,0
        );
        this.ambientLight = new Vector3f(0.01f);
    }

    @Override
    public void update(){
        super.update();
        Vector3f rot = new Vector3f(getWorldRotation()).normalize();
        lightView = new Matrix4f().lookAt(
              rot, new Vector3f(0,0,0),
                new Vector3f(0,1,0)
        );
    }

    public Matrix4f getLightSpaceMatrix(){
        return new Matrix4f(lightProjection).mul(lightView);
    }

}
