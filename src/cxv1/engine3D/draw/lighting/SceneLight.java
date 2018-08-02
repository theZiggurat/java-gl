package cxv1.engine3D.draw.lighting;

import com.sun.javafx.scene.traversal.Direction;
import org.joml.Vector3f;

import java.awt.*;

public class SceneLight {

    private Vector3f ambientLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    private Sun sun;

    public SceneLight(){
        pointLights = new PointLight[5];
        spotLights = new SpotLight[5];
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void addPointLight(PointLight pointLight){
        for(int i = 0; i<5; i++){
            if(pointLights[i] == null|| i == 4){
                pointLights[i] = pointLight;
            }
        }
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public void setPointLights(PointLight[] pointLights) {
        this.pointLights = pointLights;
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public void setSpotLights(SpotLight[] spotLights) {
        this.spotLights = spotLights;
    }

    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }
}
