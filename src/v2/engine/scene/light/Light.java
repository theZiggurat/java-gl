package v2.engine.scene.light;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.scene.ModuleNode;

@Getter
public class Light extends ModuleNode {

    private Vector3f color;
    private float intensity;


    protected Light(){

        setColor(new Vector3f(1,1,1));
        setIntensity(1);
        this.setRotation(new Vector3f(0,1,0));
        this.activate();

        if(!LightManager.registerLight(this))
            this.deactivate();

    }

    public <T extends Light> T setIntensity(float intensity){
        this.intensity = intensity;
        return (T) this;
    }

    public <T extends Light> T setColor(Vector3f color){
        this.color = color;
        return (T) this;
    }

    public <T extends Light> T setColor(float red, float green, float blue){
        return setColor(new Vector3f(red, green, blue));
    }

}
