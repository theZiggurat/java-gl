package cxv1.engine3D.draw.lighting;

import com.sun.javafx.scene.traversal.Direction;
import org.joml.Vector3f;

public class SceneLight {

    private Vector3f ambientLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    private DirectionalLight directionalLight;
    private Sun sun;

    public Vector3f getAmbientLight() {
        return ambientLight;
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

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }
}
