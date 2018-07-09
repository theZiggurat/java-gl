package cxv1.engine3D.enviorment;

import cxv1.engine3D.draw.lighting.SceneLight;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.entity.SkyBox;
import cxv1.engine3D.entity.Terrain;

public class Scene {

    private float gravityConstant = 9.81f;

    private Entity[] entities;
    private SkyBox skyBox;
    private SceneLight sceneLight;
    private Terrain terrain;

    public Entity[] getEntities(){
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
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

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

    public void setTerrain(Terrain terrain){
        this.terrain = terrain;
    }

    public Terrain getTerrain(){return terrain;}
}
