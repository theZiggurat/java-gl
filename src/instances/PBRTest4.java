package instances;

import org.joml.Vector3f;
import engine.scene.light.DirectionalLight;
import engine.scene.light.LightManager;
import engine.scene.light.PointLight;
import engine.scene.node.Node;
import engine.scene.SceneContext;
import engine.utils.AssimpLoader;
import modules.pbr.PBRMaterial;
import modules.pbr.PBRModel;

public class PBRTest4 extends SceneContext {

    @Override
    public void init() {
        Node node = new Node();

        PBRModel plane = new PBRModel(AssimpLoader.loadMeshGroup("res/models/buddha.obj").get(0),
                new PBRMaterial(1,1,1,0.1f,1));
        plane.transform.rotate(-90,0,0);
        node.addChild(plane);

        PointLight light = new PointLight();
        light.setIntensity(2f);
        light.transform.translate(0,0,1);
        node.addChild(light);

        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(0f).transform.rotateTo(0,-1,0);

        getCamera().transform.setTranslation(new Vector3f(0,0,10));

        scene.addChild(node);
    }

    @Override
    public void update(double duration) {

    }

    @Override
    public void cleanup() {

    }
}
