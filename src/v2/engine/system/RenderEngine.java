package v2.engine.system;

import lombok.Getter;
import v2.engine.scene.Scenegraph;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

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
        scenegraph.update();
    }

    public void render(){
        scenegraph.render();
    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
