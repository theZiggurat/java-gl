package v2.instances;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.light.DirectionalLight;
import v2.engine.light.LightManager;
import v2.engine.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.scene.Scenegraph;
import v2.engine.system.Camera;
import v2.engine.system.EngineCore;
import v2.engine.system.EngineInterface;
import v2.engine.system.Input;
import v2.modules.pbr.PBRModel;
import v2.modules.pbr.PBRRenderEngine;
import v2.modules.sky.Sky;

import static org.lwjgl.glfw.GLFW.*;

public class PBRTest implements EngineInterface {

    @Getter
    PBRRenderEngine pbrRenderEngine;
    Node object;

    Node model, model2, model3, model4;
    PointLight light;

    @Override
    public void init(){

        pbrRenderEngine = PBRRenderEngine.instance();

        model3 = PBRModel.quickModel("res/models/rock.obj",
                "res/images/rock/", "albedo.png", "normal.png",
                "rough.png", "metal.png", null, false, true);
        model3.scale(.1f);
        model4 = PBRModel.quickModel("res/models/doublebarrel.obj",
                "res/images/gun/", "albedo.jpg", "glnormal.jpg",
                "rough.jpg", "metal.jpg", null, false, false);
        model4.translateTo(10f,0,1).scaleTo(1f);
//        model4 = PBRModel.quickModel("res/models/sphere.obj",
//                "res/images/streaked_metal/", "albedo.png", null,
//                "rough.png", "metal.png", null, false, false);
//        model4.translateTo(7.5f,0,1).scaleTo(2f);
//        model = PBRModel.quickModel("res/models/dragon.obj",
//                "res/images/plastic_squares/", "albedo.png", "normal.png",
//                "rough.png", "metal.png", null, false, true);
//        model.translateTo(-7.5f,0,1).scaleTo(1f);

        light = new PointLight();
        light.setColor(new Vector3f(1,1,1));
        light.setTranslation(new Vector3f(0,0,0));
        light.setIntensity(1f);

        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(.5f);
        LightManager.getSun().setAmbientLight(new Vector3f(0.01f));

        object = new Node();
        object.addChildren(model4, model3, light);


        object.addChild(light);

        Scenegraph.instance().addChild(object);
    }

    double deg = 0;

    @Override
    public void update(double duration) {

        if(Input.instance().isKeyHeld(GLFW_KEY_T)){
            if(Input.instance().isKeyHeld(GLFW_KEY_LEFT_CONTROL)){
                light.setTranslation(light.getTranslation().add(new Vector3f(.01f, 0, 0)));
            } else {
                light.setTranslation(light.getTranslation().add(new Vector3f(-.01f, 0, 0)));
            }

        }

        Camera camera = EngineCore.instance().getRenderEngine().getMainCamera();

        if(Input.instance().isKeyHeld(GLFW_KEY_Q)) {
            deg-=0.05;
        }
        if(Input.instance().isKeyHeld(GLFW_KEY_E)){
            deg += 0.05;
        }

        camera.rotateAround(camera.getForward(), deg);

        if(Input.instance().isKeyHeld(GLFW_KEY_R)) {
            if(Input.instance().isKeyHeld(GLFW_KEY_LEFT_CONTROL)){
                LightManager.getSun().getRotation().rotateY(-(float)duration*.1f);
            } else {
                LightManager.getSun().getRotation().rotateY((float) duration*.1f);
            }
        }

        if(Input.instance().isKeyPressed(GLFW_KEY_RIGHT_BRACKET)){
            light.setIntensity(light.getIntensity()+1);
        }
        if(Input.instance().isKeyPressed(GLFW_KEY_LEFT_BRACKET)){
            light.setIntensity(light.getIntensity()-1);
        }

    }

//    private void buildFloor(){
//
//        int amount = 10;
//        float scale = 15;
//
//        PBRModel model;
//
//        TextureObject albedo = StaticLoader.loadTexture("res/images/wood_floor/albedo.png", false);
//        TextureObject normal = StaticLoader.loadTexture("res/images/wood_floor/normal.png", false);
//        TextureObject rough = StaticLoader.loadTexture("res/images/wood_floor/rough.png", false);
//        TextureObject metal = StaticLoader.loadTexture("res/images/wood_floor/metal.png", false);
//        for(int i = -amount/2; i<amount/2; i++){
//            for(int j = -amount/2; j<amount/2; j++){
//                model = new PBRModel(new VertexBufferObject(MeshData.loadMesh("res/models/gui.obj")),
//                        new PBRMaterial(albedo, normal, rough, metal, null));
//                model.translate(i*scale*2,-5,j*scale*2).scaleTo(scale).rotateTo(new Vector3f(90,0,0));
//                object.addChild(model);
//            }
//        }
//    }

    @Override
    public void cleanup() {

    }
}
