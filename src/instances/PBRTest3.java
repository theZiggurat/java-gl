package instances;

import engine.application.event.KeyboardEvent;
import engine.glapi.vbo.Mesh3D;
import engine.glapi.vbo.Meshs;
import engine.scene.light.DirectionalLight;
import engine.scene.light.LightManager;
import engine.scene.light.PointLight;
import engine.scene.node.Node;
import engine.system.Config;
import engine.scene.SceneContext;
import engine.utils.AssimpLoader;
import modules.pbr.PBRMaterial;
import modules.pbr.PBRModel;

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
                        case GLFW_KEY_T: t = (e.getMods() & GLFW_MOD_CONTROL) != 0 ? t + duration * 0.5 : t - duration; break;
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

        PBRMaterial fu = new PBRMaterial("res/images/scuffed_plastic/", "albedoyellow.png",
                "normal.png", "rough.png", "metal.png",  false);

        PBRModel dragon1 = new PBRModel(mesh,fu);
        PBRModel dragon2 = new PBRModel(mesh, new PBRMaterial("res/images/scuffed_plastic/",
                "albedowhite.png", "normal.png", "rough.png", "metal.png",  false));
        dragon1.transform.scaleTo(.8f).translate(0f,0,-22);
        dragon2.transform.scaleTo(.8f).translate(-13f,0,-22).rotateTo((float)(Math.PI),0,0);
        object.addChildren(dragon1, dragon2);

        buildFloor();
        buildWalls();

        /** material testers **/
        Node matTesters = new Node();
        Mesh3D m = AssimpLoader.loadMeshGroup("res/models/mat_test.obj").get(0);
        PBRModel m1 = new PBRModel(m, new PBRMaterial(0.15f, 0.488f, 0.5f, 0.95f, 0f));
        m1.transform.translate(0,0,-15).scaleTo(2f);
        PBRModel m2 = new PBRModel(m, new PBRMaterial("res/images/chipped_paint/", false));
        m2.transform.translate(0,0,-10).scaleTo(2f);
        PBRModel m3 = new PBRModel(m, new PBRMaterial("res/images/scuffed_plastic/",
                "albedoblue.png", "normal.png", "rough.png", "metal.png",  false));
        m3.transform.translate(0,0,-5).scaleTo(2f);
        PBRModel m4 = new PBRModel(m, new PBRMaterial(0.677f, 0.7f, 0.85f, 0.08f, 1f));
        m4.transform.translate(0,0,0).scaleTo(2f);
        PBRModel m5 = new PBRModel(m, new PBRMaterial("res/images/plastic_squares/", false));
        m5.transform.translate(0,0,5).scaleTo(2f);

        matTesters.transform.translate(12, -m1.getMesh().getLowest(), 0);
        matTesters.addChildren(m1, m2, m3, m4, m5);

        /** bunny and buddha **/
        m = AssimpLoader.loadMeshGroup("res/models/bunny.obj").get(0);
        PBRModel bunny = new PBRModel(m, new PBRMaterial("res/images/scuffed_plastic/",
                "albedored.png", "normal.png", "rough.png", "metal.png",  false));
        bunny.transform.translate(-25, -m.getLowest(), -10).scaleTo(1.5f).rotate(0, -90, 0);

        m = AssimpLoader.loadMeshGroup("res/models/buddha.obj").get(0);
        PBRModel buddha = new PBRModel(m, new PBRMaterial("res/images/gold/", true));
        buddha.transform.translate(-25f,-2.5f,0).rotate(-90, 0, 90).scale(5f);

        object.addChildren(matTesters, bunny, buddha);

        //object.transform.translate(0,0,3);

        PointLight light = new PointLight();
        light.setColor(0.1f,0.5f,1f).setIntensity(20f).transform.translate(-10,0,-10);
        lights.addChild(light);
//        lights.addChildren(light, new PointLight(light).transform.translateTo(-10, 0 ,-20),
//                                  new PointLight(light).transform.translateTo(-20, 0 ,-20),
//                                  new PointLight(light).transform.translateTo(-20, 0 ,-10));

        //lights.addChild(light);
        lights.transform.translate(0,1.5f,0);


        LightManager.setSun(new DirectionalLight());
        LightManager.getSun().setIntensity(1.2f).transform.rotateTo(0,-1,0);

        scene.addChildren(object);
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

        lights.transform.translateTo((float) (Math.sin(q/4))*20,1f,0f);

        LightManager.getSun().transform.rotateTo((float) (Math.sin(t) * 0.5),0.5f,(float) -(Math.cos(t)*0.5));
    }

    private void buildFloor(){

        int amount = 4;
        float scale = 6;

        PBRModel model;
        PBRMaterial mat = new PBRMaterial("res/images/scuffed_plastic/",
                "albedoblack.png", "normal.png", "rough.png", "metal.png", false);
        mat.setMetalConst(0.85f);
        mat.useMetalMap(false);

        PBRMaterial mat2 = new PBRMaterial("res/images/scuffed_plastic/",
                "albedoblue.png", "normal.png", "rough.png", "metal.png", false);
        mat2.setMetalConst(1f);
        mat2.useMetalMap(false);

//        PBRMaterial mat = new PBRMaterial(0,0,0,0,1);
//        PBRMaterial mat2 = mat;

        for(int i = -amount/2; i<amount/2; i++){
            for(int j = -amount/2; j<amount/2; j++){

                if((i == -1||i==0)&&(j==-1||j==0)) model = new PBRModel(Meshs.thickquad, mat2);
                else model = new PBRModel(Meshs.thickquad, mat);
                model.transform.translate(i*scale*2,0,j*scale*2).scaleTo(scale).rotateTo(90,0,0);
                object.addChild(model);
            }
        }
    }

    private void buildWalls(){
        PBRModel model;

        PBRMaterial mat = new PBRMaterial("res/images/concrete/", false);

        float scale = 6;
        int amount = 4;

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.thickquad, mat);
            model.transform.translate(2*scale*(i-amount/2.0f),-model.getMesh().getLowest()*scale-.1f,-(1+amount)*scale).scaleTo(scale);
            model.setUVscalar(1f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.thickquad, mat);
            model.transform.translate(-(1+amount)*scale,-model.getMesh().getLowest()*scale-.1f,2*scale*(i-amount/2.0f))
                            .scaleTo(scale)
                            .rotateTo(0,-90,0);
            model.setUVscalar(1f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.thickquad, mat);
            model.transform.translate(2*scale*(i-amount/2.0f),-model.getMesh().getLowest()*scale-.1f,(amount-1)*scale)
                    .scaleTo(scale)
                    .rotateTo(0,180,0);
            model.setUVscalar(1f);
            object.addChild(model);
        }

        for(int i = 0; i < amount; i++){
            model = new PBRModel(Meshs.thickquad, mat);
            model.transform.translate((amount-1)*scale,-model.getMesh().getLowest()*scale-.1f,2*scale*(i-amount/2.0f))
                    .scaleTo(scale)
                    .rotateTo(0,90,0);
            model.setUVscalar(1f);
            object.addChild(model);
        }

    }

    @Override
    public void cleanup() {

    }
}
