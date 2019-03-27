package v2.instances;

import org.joml.Vector3f;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.light.DirectionalLight;
import v2.engine.scene.light.LightManager;
import v2.engine.scene.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.system.*;
import v2.engine.utils.AssimpLoader;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;

import static org.lwjgl.glfw.GLFW.*;

public class PBRTest extends Context {

    Node object;

    Node model3, model4, model5, model6;
    PointLight light;

    public void init(){

//        model3 = PBRModel.quickModel("res/models/sphere.obj",
//                "res/images/misc/", "gare.jpg", null,
//                null, null, null, false, false);
//        model3.scale(5f);
        model3 = new PBRModel(AssimpLoader.loadMeshGroup("res/models/glock.obj"),
                new PBRMaterial("res/images/glock/", "albedo.png", "normal.png",
                "rough.png", "metal.png", false));
        model3.translateTo(10f,0,1).scale(.2f);


        model4 = new PBRModel(Meshs.sphere,
                new PBRMaterial("res/images/plastic_squares/", "albedo.png", "normal.png",
                "rough.png", "metal.png", false));
        model4.translateTo(7.5f,0,1).scaleTo(2f);

        model5 = new PBRModel(Meshs.sphere,
                new PBRMaterial(.4f, .3f, 1f, 1f, 1f));
        model5.translate(4,0,1);

        model6 = new PBRModel(Meshs.sphere,
                new PBRMaterial(0f, 1f, 1f, 0f, 0f));
        model6.translate(2,0, 1);

        light = new PointLight();
        light.setColor(new Vector3f(1,1,1));
        light.translate(new Vector3f(0,0,0));
        light.setIntensity(2f);

        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(.8f);
        LightManager.getSun().setAmbientLight(new Vector3f(0.01f));

        object = new Node();
        object.addChildren(model4, model3, model6, model5, light);

        scene.addChild(object);
    }

    double deg = 0;

    @Override
    public void update(double duration) {

        if(Input.instance().isKeyHeld(GLFW_KEY_T)){
            if(Input.instance().isKeyHeld(GLFW_KEY_LEFT_CONTROL)){
                light.translate(light.getTranslation().add(new Vector3f(.01f, 0, 0)));
            } else {
                light.translate(light.getTranslation().add(new Vector3f(-.01f, 0, 0)));
            }

        }

        Camera camera = Core.camera();

        if(Input.instance().isKeyHeld(GLFW_KEY_Q)) {
            deg-=0.05;
        }
        if(Input.instance().isKeyHeld(GLFW_KEY_E)){
            deg += 0.05;
        }

        camera.rotateAround(camera.getForward(), deg);

        if(Input.instance().isKeyHeld(GLFW_KEY_R)) {
            if(Input.instance().isKeyHeld(GLFW_KEY_LEFT_CONTROL)){
                LightManager.getSun().getRotation().rotateY(-(float)duration*.4f);
            } else {
                LightManager.getSun().getRotation().rotateY((float) duration*.4f);
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
