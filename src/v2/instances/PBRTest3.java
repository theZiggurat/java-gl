package v2.instances;

import v2.engine.application.event.KeyboardEvent;
import v2.engine.glapi.vbo.Mesh3D;
import v2.engine.glapi.vbo.Meshs;
import v2.engine.glapi.vbo.VertexBufferObject;
import v2.engine.scene.light.DirectionalLight;
import v2.engine.scene.light.LightManager;
import v2.engine.scene.light.PointLight;
import v2.engine.scene.node.Node;
import v2.engine.system.Config;
import v2.engine.scene.SceneContext;
import v2.engine.utils.AssimpLoader;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;
import v2.modules.sky.Sky;

import static org.lwjgl.glfw.GLFW.*;

public class PBRTest3 extends SceneContext {

    Node object, lights;
    double duration;

    @Override
    public void init() {

        onEvent(k -> {

            if(k instanceof KeyboardEvent) {
                KeyboardEvent e = (KeyboardEvent) k;
                if(e.getAction()==KeyboardEvent.KEY_PRESSED) {
                    switch (e.getKey()) {
                        case GLFW_KEY_F1: {Config.instance().setWireframe(!Config.instance().isWireframe()); break;}
                        case GLFW_KEY_F2: {Config.instance().setDebugLayer(!Config.instance().isDebugLayer()); break;}
                        case GLFW_KEY_E: {move = !move; break;}
                        default: return;
                    }

                } else if(e.getAction()==KeyboardEvent.KEY_HELD){
                    switch (e.getKey()) {
                        case GLFW_KEY_T: t = (e.getMods() & GLFW_MOD_CONTROL) != 0 ? t + duration : t - duration; break;
                        default: return;
                    }
                }
                e.consume();
            }
        });

        object = new Node();
        lights = new Node();
        object.addChild(lights);

        Mesh3D mesh = AssimpLoader.loadMeshGroup("res/models/dragon.obj").get(0);

        PBRModel dragon1 = new PBRModel(mesh, new PBRMaterial("res/images/scuffed_plastic/",
                "albedoyellow.png", "normal.png", "rough.png", "metal.png",  false));
        PBRModel dragon2 = new PBRModel(mesh, new PBRMaterial("res/images/scuffed_plastic/",
                "albedowhite.png", "normal.png", "rough.png", "metal.png",  false));
        dragon1.transform.scaleTo(.8f).translate(-7.5f,0,-15);
        dragon2.transform.scaleTo(.8f).translate(-22.5f,0,-15).rotateTo((float)(Math.PI),0,0);
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
        s1.transform.translate(-20,2,-5f);
        s2.transform.translate(-18,2,-5f);
        s3.transform.translate(-16,2,-5f);
        s4.transform.translate(-14,2,-5f);
        s10.transform.translate(-12,2,-5f);

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
        s5.transform.translate(-20,2,0f);
        s6.transform.translate(-18,2,0f);
        s7.transform.translate(-16,2,0f);
        s8.transform.translate(-14,2,0f);
        s9.transform.translate(-12,2,0f);


        Mesh3D m = AssimpLoader.loadMeshGroup("res/models/mat_test.obj").get(0);
        PBRModel m1 = new PBRModel(m, new PBRMaterial(0.2f, 0.6f, 1.0f, 0.1f, 0f));
        m1.transform.translate(7,-m1.getMesh().getLowest(),-15).scaleTo(2f);
        PBRModel m2 = new PBRModel(m, new PBRMaterial("res/images/cloth/", true));
        m2.transform.translate(7,-m1.getMesh().getLowest(),-10).scaleTo(2f);
        PBRModel m3 = new PBRModel(m, new PBRMaterial("res/images/scuffed_plastic/",
                "albedoblue.png", "normal.png", "rough.png", "metal.png",  false));
        m3.transform.translate(7,-m1.getMesh().getLowest(),-5).scaleTo(2f);
        PBRModel m4 = new PBRModel(m, new PBRMaterial(0.1f, 0.1f, 0.1f, 0.5f, 1f));
        m4.transform.translate(7,-m1.getMesh().getLowest(),0).scaleTo(2f);
        PBRModel m5 = new PBRModel(m, new PBRMaterial(0.1f, 0.6f, 0.6f, 0.8f, 0f));
        m5.transform.translate(7,-m1.getMesh().getLowest(),5).scaleTo(2f);

        object.addChildren(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, m1, m2, m3, m4, m5);

        object.transform.translate(0,0,3);

        PointLight light = new PointLight();
        light.setColor(0.1f,0.5f,1f).setIntensity(20f).transform.translate(-10,0,-10);
        lights.addChild(light);
//        lights.addChildren(light, new PointLight(light).transform.translateTo(-10, 0 ,-20),
//                                  new PointLight(light).transform.translateTo(-20, 0 ,-20),
//                                  new PointLight(light).transform.translateTo(-20, 0 ,-10));

        //lights.addChild(light);
        lights.transform.translate(0,1.5f,0);


        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(0f).transform.rotateTo(0,-1,0);

        scene.addChildren(object, Sky.instance());
        camera.transform.translateTo(-10,7,15);
    }

    double t, q;
    boolean move = false;

    @Override
    public void update(double duration) {

        this.duration = duration/200;

        LightManager.getSun().update();
        if(move)
            q+=this.duration;

        lights.transform.translateTo((float) (Math.sin(q/2) * 12),1.5f,0);

        LightManager.getSun().transform.rotateTo((float) (Math.sin(t) * 0.5),0.5f,(float) (Math.cos(t)*0.5));
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
                model.transform.translate(i*scale*2,0,j*scale*2).scaleTo(scale).rotateTo(90,0,0);
                object.addChild(model);
            }
        }
    }

    private void buildWalls(){
        PBRModel model;
        PBRMaterial mat = new PBRMaterial("res/images/green_tiles/",  false);
        mat.useMetalMap(false);
        mat.setMetalConst(0f);

        float scale = 7;
        int amount = 4;

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.quad, mat);
            model.transform.translate(2*scale*(i-amount/2.0f),-model.getMesh().getLowest()*scale-.1f,-(1+amount)*scale).scaleTo(scale);
            model.setUVscalar(2f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.quad, mat);
            model.transform.translate(-(1+amount)*scale,-model.getMesh().getLowest()*scale-.1f,2*scale*(i-amount/2.0f))
                            .scaleTo(scale)
                            .rotateTo(0,-90,0);
            model.setUVscalar(2f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.quad, mat);
            model.transform.translate(2*scale*(i-amount/2.0f),-model.getMesh().getLowest()*scale-.1f,(amount-1)*scale)
                    .scaleTo(scale)
                    .rotateTo(0,180,0);
            model.setUVscalar(2f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.quad, mat);
            model.transform.translate((amount-1)*scale,-model.getMesh().getLowest()*scale-.1f,2*scale*(i-amount/2.0f))
                    .scaleTo(scale)
                    .rotateTo(0,90,0);
            model.setUVscalar(2f);
            object.addChild(model);
        }

    }

    @Override
    public void cleanup() {

    }
}
