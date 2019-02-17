package v2.instances;

import v2.engine.light.DirectionalLight;
import v2.engine.light.LightManager;
import v2.engine.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.scene.Scenegraph;
import v2.engine.system.EngineInterface;
import v2.engine.gldata.vbo.Meshs;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;

public class PBRTest2 implements EngineInterface {

    PointLight light1, light2, light3, light4;
    Node object, lights;


    @Override
    public void init() {


        object = new Node();
        lights = new Node();
        object.addChild(lights);

        int matrixdim = 5;
        for(int i = 0; i < matrixdim; i++){
            for(int j = 0; j < matrixdim; j++){
                PBRModel model = new PBRModel(Meshs.sphere,
                        new PBRMaterial(1f, 1f, .1f,
                                (float)i/(float)matrixdim, (float)j/(float)matrixdim));
                model.translate(0, 2*(i-matrixdim/2), 2*(j-matrixdim/2));
                object.addChild(model);
            }
        }

        light1 = new PointLight();
        light1.setIntensity(5f);
        light1.translate(2,2,2);

        light2 = new PointLight(light1).translate(2,2,-2);
        light3 = new PointLight(light1).translate(2,-2,-2);
        light4 = new PointLight(light1).translate(2,-2,2);

        LightManager.setSun(new DirectionalLight().setIntensity(0f).rotateTo(1,0,0));

        lights.addChildren(light1, light2, light3, light4);

        Scenegraph.instance().addChild(object);
    }

    double time = 0;

    @Override
    public void update(double duration) {
        time += duration;
        lights.translateTo(0, (float)(2*Math.cos(time*10))-2, (float)(2*Math.sin(time*10)));
    }

    @Override
    public void cleanup() {

    }
}
