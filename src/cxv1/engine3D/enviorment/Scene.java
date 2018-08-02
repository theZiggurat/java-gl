package cxv1.engine3D.enviorment;

import cxv1.engine3D.draw.lighting.SceneLight;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.entity.SkyBox;
import cxv1.engine3D.entity.Terrain;

import java.util.HashMap;

public class Scene {

    private float gravityConstant = 9.81f;

    private String name;
    private HashMap<String, Entity> entities;
    private SkyBox skyBox;
    private SceneLight sceneLight;
    private Terrain terrain;

    public Scene(){
        entities = new HashMap<>();
        sceneLight = new SceneLight();
    }

    // GETTER AND SETTER // ----------------------------------

    public HashMap<String, Entity> getEntities(){
        return entities;
    }
    public SkyBox getSkyBox() {
        return skyBox;
    }
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }
    public SceneLight getSceneLight() {
        return sceneLight;
    }
    public float getGravityConstant(){
        return gravityConstant;
    }
    public void setGravityConstant(float constant){
        this.gravityConstant = constant;
    }
    public void setSceneLight(SceneLight sceneLight) { this.sceneLight = sceneLight; }
    public void setTerrain(Terrain terrain){
        this.terrain = terrain;
    }
    public Terrain getTerrain(){return terrain;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void setEntities(HashMap<String, Entity> entities) { this.entities = entities; }
}
