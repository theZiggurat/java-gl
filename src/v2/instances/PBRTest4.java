package v2.instances;

import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.light.DirectionalLight;
import v2.engine.scene.light.LightManager;
import v2.engine.scene.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.system.Context;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;

public class PBRTest4 extends Context {

    @Override
    public void init() {
        Node node = new Node();

        PBRModel plane = new PBRModel(Meshs.quad, new PBRMaterial(
                1,1,1,0,0
        ));
        node.addChild(plane);

        PointLight light = new PointLight();
        light.setIntensity(2f);
        light.translate(0,0,1);
        node.addChild(light);

        LightManager.setSun(new DirectionalLight().setIntensity(0f).rotateTo(0,-1,0));

        scene.addChild(node);
    }

    @Override
    public void update(double duration) {

    }

    @Override
    public void cleanup() {

    }
}
