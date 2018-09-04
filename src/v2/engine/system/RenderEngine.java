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

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
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

    TextureObject texture;
    TextureObject curr;

    public void init(){
        Window window = Window.getInstance();
        scenegraph.update();
        deferredFBO = new DeferredFBO(window.getWidth(), window.getHeight(),1);
        deferredRenderer = new PBRDeferredRenderer();
        quad = new FSQuad();
        texture = StaticLoader.loadTexture("res/images/woodframe/normal.png");
    }

    public void render(){



        mainCamera.update();

        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(Window.getInstance().isResized()){
            glViewport(0,0, Window.getInstance().getWidth(),
                    Window.getInstance().getHeight());
            Window.getInstance().setResized(false);
        }

        deferredFBO.bind();
        scenegraph.render();
        deferredFBO.unbind();

        if(InputCore.getInstance().isKeyHeld(GLFW_KEY_ENTER)){
            curr = deferredFBO.getAlbedo();
        } else {
            curr = texture;
        }

        quad.setScreenTexture(curr);
        quad.render();


    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
