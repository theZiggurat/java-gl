package v2.engine.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.scene.ModuleNode;

@Getter @Setter
public class Light extends ModuleNode {

    private Vector3f color;
    private float intensity;


    protected Light(){

        setColor(new Vector3f(1,1,1));
        setIntensity(1);
        this.setRotation(new Vector3f(0,-1,0));
        this.activate();

        if(!LightManager.registerLight(this))
            this.deactivate();



    }

    @Override
    public void render(){
        if(this.isActivated()){

        }
    }


}
