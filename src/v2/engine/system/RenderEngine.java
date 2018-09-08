package v2.engine.system;

import jdk.internal.util.xml.impl.Input;
import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.FrameBufferObject;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Scenegraph;
import v2.modules.deferred.DeferredFBO;
import v2.modules.deferred.FSQuad;
import v2.modules.pbr.PBRDeferredRenderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

    private DeferredFBO deferredFBO;

    private PBRDeferredRenderer deferredRenderer;
    private FSQuad quad;

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
        Window window = Window.getInstance();
        scenegraph.update();
        deferredFBO = new DeferredFBO(window.getWidth(), window.getHeight(),1);
        deferredRenderer = new PBRDeferredRenderer();
        quad = new FSQuad();
    }

    TextureObject currTexture;

    public void render(){



        mainCamera.update();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        deferredFBO.bind();
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        deferredFBO.unbind();

        if(Window.getInstance().isResized()){
            glViewport(0,0, Window.getInstance().getWidth(),
                    Window.getInstance().getHeight());
            Window.getInstance().setResized(false);
        }



        deferredFBO.bind();
        scenegraph.render();
        deferredFBO.unbind();


         if (InputCore.getInstance().isKeyHeld(GLFW_KEY_2)){
            quad.setScreenTexture(deferredFBO.getPosition());
        } else if (InputCore.getInstance().isKeyHeld(GLFW_KEY_3)){
            quad.setScreenTexture(deferredFBO.getNormal());
        } else {
            quad.setScreenTexture(deferredFBO.getAlbedo());
        }




        quad.render();


    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
