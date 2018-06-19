package cxv1.engine3D.enviorment;

import cxv1.engine3D.draw.lighting.SceneLight;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.entity.SkyBox;

public class Scene {

    private Entity[] entities;
    private SkyBox skyBox;
    private SceneLight sceneLight;

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

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
}
