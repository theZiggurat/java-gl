package v2.instances;

import v2.engine.gldata.vbo.Mesh3D;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.light.DirectionalLight;
import v2.engine.scene.light.LightManager;
import v2.engine.scene.light.PointLight;
import v2.engine.scene.Node;
import v2.engine.system.Config;
import v2.engine.system.Context;
import v2.engine.event.Input;
import v2.engine.utils.AssimpLoader;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;
import v2.modules.sky.Sky;

import static org.lwjgl.glfw.GLFW.*;

public class PBRTest3 extends Context {

    Node object, lights;

    @Override
    public void init() {

        object = new Node();
        lights = new Node();
        object.addChild(lights);

        Mesh3D mesh = AssimpLoader.loadMeshGroup("res/models/dragon.obj").get(0);

        PBRModel dragon1 = new PBRModel(mesh, new PBRMaterial(
                .4f,0f,0f,0.5f, 0f
        ));
        PBRModel dragon2 = new PBRModel(mesh, new PBRMaterial(
                .9f,.7f,.8f,0.1f, 0f
        ));
        dragon1.scaleTo(.8f).translate(-7.5f,0,-15);
        dragon2.scaleTo(.8f).translate(-22.5f,0,-15).rotateTo((float)(Math.PI),0,0);
        object.addChildren(dragon1, dragon2);

        buildFloor();
        buildWalls();

        PBRModel s1 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,0.1f,0
        ));
        PBRModel s2 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,0.3f,0
        ));
        PBRModel s3 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,0.5f,0
        ));
        PBRModel s4 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,0.8f,0
        ));
        PBRModel s10 = new PBRModel(Meshs.sphere, new PBRMaterial(
                1,1,1,1f,0
        ));
        s1.translate(0,2,-5f);
        s2.translate(2,2,-5f);
        s3.translate(4,2,-5f);
        s4.translate(6,2,-5f);
        s10.translate(8,2,-5f);

        PBRModel s5 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0.5f,0.5f,0.5f,0.1f,1
        ));
        PBRModel s6 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0.5f,0.5f,0.5f,0.3f,1
        ));
        PBRModel s7 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0.5f,0.5f,0.5f,0.5f,1
        ));
        PBRModel s8 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0.5f,0.5f,0.5f,0.8f,1
        ));
        PBRModel s9 = new PBRModel(Meshs.sphere, new PBRMaterial(
                0.5f,0.5f,0.5f,1f,1
        ));
        s5.translate(0,2,0f);
        s6.translate(2,2,0f);
        s7.translate(4,2,0f);
        s8.translate(6,2,0f);
        s9.translate(8,2,0f);



        PBRModel mat_test = new PBRModel(AssimpLoader.loadMeshGroup("res/models/mat_test.obj").get(0),
                new PBRMaterial(0.8f, 0.9f, 1.0f, 0.1f, 0f));
        mat_test.translate(7,-mat_test.getMesh().getLowest(),-15).scaleTo(2f);

        object.addChildren(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, mat_test);

        object.translate(0,0,3);

        PointLight light = new PointLight().setColor(1f,.2f,0.2f).setIntensity(20f).translate(-10,0,-10);
        lights.addChildren(light, new PointLight(light).translateTo(-10, 0 ,-20),
                                  new PointLight(light).translateTo(-20, 0 ,-20),
                                  new PointLight(light).translateTo(-20, 0 ,-10));

        //lights.addChild(light);
        lights.translate(0,1.5f,0);


        LightManager.setSun(new DirectionalLight().setIntensity(1.1f).rotateTo(0,-1,0));

        scene.addChildren(object, Sky.instance());
        camera.translateTo(-10,7,15);
    }

    double t, q;
    boolean move = false;

    @Override
    public void update(double duration) {

        LightManager.getSun().update();

        if(Input.instance().isKeyPressed(GLFW_KEY_F1))
            Config.instance().setWireframe(!Config.instance().isWireframe());

        if(Input.instance().isKeyPressed(GLFW_KEY_F2))
            Config.instance().setDebugLayer(!Config.instance().isDebugLayer());

        if(Input.instance().isKeyPressed(GLFW_KEY_E))
            move = !move;

        if(move)
            q+=duration;

        lights.translateTo((float) (Math.sin(q) * 12)+ 5f,1.5f,0);

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
