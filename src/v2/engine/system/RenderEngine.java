package v2.engine.system;

import v2.engine.scene.Scenegraph;

public class RenderEngine {

    private Scenegraph scenegraph;


    public static RenderEngine instance = null;

    public static RenderEngine getInstance(){
        if(instance == null){
            instance = new RenderEngine();
        }
        return instance;
    }

    private RenderEngine(){
        scenegraph = new Scenegraph();

    }

    public void init(){

    }

    public void render(){

    }


}
