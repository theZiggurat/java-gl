package v2.engine.system;

import com.sun.xml.internal.ws.api.pipe.Engine;
import lombok.Getter;
import lombok.Setter;
import v2.engine.scene.Scenegraph;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

    @Setter @Getter private Camera mainCamera;

    public static RenderEngine instance = null;

    public static RenderEngine getInstance(){
        if(instance == null){
            instance = new RenderEngine();
        }
        return instance;
    }

    private RenderEngine(){
        scenegraph = new Scenegraph();
        mainCamera = new Camera();
    }

    public void init(){
        scenegraph.update();
    }

    public void render(){

        mainCamera.update();

        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(EngineCore.getInstance().getWindow().isResized()){
            glViewport(0,0, EngineCore.getInstance().getWindow().getWidth(),
                    EngineCore.getInstance().getWindow().getHeight());
            EngineCore.getInstance().getWindow().setResized(false);
        }

        scenegraph.render();
    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
