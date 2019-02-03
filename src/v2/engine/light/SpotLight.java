package v2.engine.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class SpotLight extends Light {

    @Getter @Setter
    private Vector3f direction;
    @Getter @Setter
    private float radius;

    public SpotLight(){
        super();
        this.direction = new Vector3f(0,-1,0);
        this.radius = 1f;
    }
}
