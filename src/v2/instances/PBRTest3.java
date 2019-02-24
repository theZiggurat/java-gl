package v2.instances;

import v2.engine.gldata.vbo.Mesh3D;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.light.DirectionalLight;
import v2.engine.light.LightManager;
import v2.engine.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.scene.Scenegraph;
import v2.engine.system.Config;
import v2.engine.system.EngineInterface;
import v2.engine.system.Input;
import v2.engine.utils.AssimpLoader;
import v2.engine.utils.ImageLoader;
import v2.modules.debug.Line;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;
import v2.modules.pbr.PBRRenderEngine;
import v2.modules.sky.Sky;
import v2.modules.sky.SkyShaderProgram;

import static org.lwjgl.glfw.GLFW.*;

public class PBRTest3 implements EngineInterface {

    Node object, lights;
    PBRModel cube;

    @Override
    public void init() {
        object = new Node();
        lights = new Node();
        object.addChild(lights);

        Mesh3D mesh = AssimpLoader.loadMeshGroup("res/models/dragon.obj").get(0);

        PBRModel dragon1 = new PBRModel(mesh, new PBRMaterial(
                0f,0f,0f,1f, 0f
        ));
        PBRModel dragon2 = new PBRModel(mesh, new PBRMaterial(
                .5f,0f,0f,0.5f, 0f
        ));
        dragon1.scaleTo(.8f).translate(-7.5f,0,-15);
        dragon2.scaleTo(.8f).translate(-22.5f,0,-15).rotateTo((float)(Math.PI),0,0);
        object.addChildren(dragon1, dragon2);

        buildFloor();
        buildWalls();

        cube = new PBRModel(Meshs.cube, new PBRMaterial(
                "res/images/plastic_squares/", false
        ));
        cube.scale(3f).translate(-3,3f,10);

        PBRModel s1 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0,0,0,0.3f,0
        ));
        PBRModel s2 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,0.5f,0
        ));
        s1.translate(0,1,-15f);
        s2.translate(0,1,-13f);

        PBRModel nightstand = new PBRModel(AssimpLoader.loadMeshGroup("res/models/Nightstand.obj").get(0),
                new PBRMaterial("res/images/night_stand/", "albedo.png", "normal.png", "rough.png", "metal.png", true));
        nightstand.translate(7,-nightstand.getMesh().getLowest(),-15).scaleTo(8f);

        object.addChildren(cube, s1, s2, nightstand);

        object.translate(0,0,3);

        PointLight light = new PointLight().setColor(0,.7f,1).setIntensity(7f).translate(-10,0,-10);
        lights.addChildren(light, new PointLight(light).translateTo(-10, 0 ,-20),
                                  new PointLight(light).translateTo(-20, 0 ,-20),
                                  new PointLight(light).translateTo(-20, 0 ,-10));

        //lights.addChild(light);
        lights.translate(0,1.5f,0);


        LightManager.setSun(new DirectionalLight().setIntensity(1.1f).rotateTo(0,-1,0));

        Scenegraph.instance().addChildren(object, Sky.instance());
        PBRRenderEngine.instance().getMainCamera().translateTo(-14,3,-9);
    }

    double t;

    @Override
    public void update(double duration) {

        LightManager.getSun().update();

        if(Input.instance().isKeyPressed(GLFW_KEY_F1))
            Config.instance().setSsao(!Config.instance().isSsao());

        if(Input.instance().isKeyPressed(GLFW_KEY_F2))
            Config.instance().setDebugLayer(!Config.instance().isDebugLayer());

//        if(Input.instance().isKeyHeld(GLFW_KEY_Q))
//            cube.rotate(0.05f, 0,0);

        if(Input.instance().isKeyHeld(GLFW_KEY_E))
            cube.rotate(0, 0.05f,0);

        if(Input.instance().isKeyHeld(GLFW_KEY_T)){
            if(Input.instance().isKeyHeld(GLFW_KEY_LEFT_CONTROL))
                t+=duration;
            else
                t-=duration;
        }
        LightManager.getSun().rotateTo((float) (Math.sin(t) * 0.5),1,(float) (Math.cos(t)*0.5));
    }

    private void buildFloor(){

        int amount = 4;
        float scale = 7;

        PBRModel model;
        PBRMaterial mat = new PBRMaterial("res/images/white_marble/", "tga", false);
        mat.useMetalMap(false);
        mat.setMetalConst(0f);

        for(int i = -amount/2; i<amount/2; i++){
            for(int j = -amount/2; j<amount/2; j++){
                model = new PBRModel(Meshs.quad, mat);
                model.translate(i*scale*2,0,j*scale*2).scaleTo(scale).rotateTo(90,0,0);
                object.addChild(model);
            }
        }
    }

    private void buildWalls(){
        PBRModel model;
        PBRMaterial mat = new PBRMaterial("res/images/black_marble/", "tga", false);
        mat.useMetalMap(false);
        mat.setMetalConst(0f);

        float scale = 7;
        int amount = 4;

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.quad, mat);
            model.translate(2*scale*(i-amount/2.0f),-model.getMesh().getLowest()*scale,-25).scaleTo(scale);
            object.addChild(model);
        }

    }

    @Override
    public void cleanup() {

    }
}
