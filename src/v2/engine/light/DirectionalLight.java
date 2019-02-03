package v2.engine.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class DirectionalLight extends Light {

    @Getter @Setter
    private Vector3f ambientLight;

    public DirectionalLight(){
        super();
        this.ambientLight = new Vector3f(0.05f,0.05f,0.05f);
    }

}
