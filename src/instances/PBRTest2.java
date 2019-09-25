package instances;

import engine.scene.light.DirectionalLight;
import engine.scene.light.LightManager;
import engine.scene.light.PointLight;
import engine.scene.node.Node;
import engine.scene.SceneContext;
import engine.glapi.vbo.Meshs;
import engine.application.event.InputManager;
import modules.pbr.PBRMaterial;
import modules.pbr.PBRModel;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class PBRTest2 extends SceneContext {

    PointLight light1;
    Node object, lights;


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
                model.transform.translate(0, 2*(i-matrixdim/2), 2*(j-matrixdim/2));
                object.addChild(model);
            }
        }

        light1 = new PointLight();
        light1.setIntensity(5f);
        light1.transform.translate(2,2,2);

        //light2 = new PointLight(light1).translate(2,2,-2);
        //light3 = new PointLight(light1).translate(2,-2,-2);
        //light4 = new PointLight(light1).translate(2,-2,2);

        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(1f).transform.rotateTo(0,-1,0);

        lights.addChildren(light1);

        scene.addChild(object);
    }

    double time = 0;
    boolean rotate;

    public void update(double duration) {

        if(InputManager.instance().isKeyHeld(GLFW_KEY_SPACE))
            rotate = !rotate;

        if(rotate) {
            time += duration;
            lights.transform.translateTo(0, (float) (2 * Math.cos(time)) - 2, (float) (2 * Math.sin(time)));
        }
    }

    public void cleanup() {

    }
}
