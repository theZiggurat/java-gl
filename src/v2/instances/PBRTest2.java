package v2.instances;

import v2.engine.light.DirectionalLight;
import v2.engine.light.LightManager;
import v2.engine.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.scene.Scenegraph;
import v2.engine.system.EngineInterface;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.system.Input;
import v2.engine.utils.ImageLoader;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class PBRTest2 implements EngineInterface {

    PointLight light1, light2, light3, light4;
    Node object, lights;


    @Override
    public void init() {


        object = new Node();
        lights = new Node();
        object.addChild(lights);

//        PBRMaterial mat = new PBRMaterial();
//        mat.setMetalMap(ImageLoader.loadTexture("res/images/plastic_squares/metal.png", false));
//        mat.setAlbedoMap(ImageLoader.loadTexture("res/images/plastic_squares/albedo.png", false));
//        mat.setRoughnessMap(ImageLoader.loadTexture("res/images/plastic_squares/rough.png", false));
//        mat.setNormalMap(ImageLoader.loadTexture("res/images/plastic_squares/normal.png", false));
//        mat.useMetalMap(true);
//        mat.useRoughnessMap(true);
//        mat.useAlbedoMap(true);
//        mat.useNormalMap(true);


        int matrixdim = 5;
        for(int i = 0; i < matrixdim; i++){
            for(int j = 0; j < matrixdim; j++){
                PBRModel model = new PBRModel(Meshs.sphere,
                        new PBRMaterial(0f, 0f, 0f,
                                (float)i/(float)matrixdim, (float)j/(float)matrixdim));
                model.translate(0, 2*(i-matrixdim/2), 2*(j-matrixdim/2));
                object.addChild(model);
            }
        }

        light1 = new PointLight();
        light1.setIntensity(5f);
        light1.translate(2,2,2);

        //light2 = new PointLight(light1).translate(2,2,-2);
        //light3 = new PointLight(light1).translate(2,-2,-2);
        //light4 = new PointLight(light1).translate(2,-2,2);

        LightManager.setSun(new DirectionalLight().setIntensity(0f).rotateTo(0,-1,0));

        lights.addChildren(light1);

        Scenegraph.instance().addChild(object);
    }

    double time = 0;
    boolean rotate;

    @Override
    public void update(double duration) {

        if(Input.instance().isKeyHeld(GLFW_KEY_SPACE))
            rotate = !rotate;

        if(rotate) {
            time += duration;
            lights.translateTo(0, (float) (2 * Math.cos(time)) - 2, (float) (2 * Math.sin(time)));
        }
    }

    @Override
    public void cleanup() {

    }
}
