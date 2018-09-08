package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Scenegraph;
import v2.modules.deferred.DeferredFBO;
import v2.modules.deferred.FSQuad;
import v2.modules.pbr.PBRDeferredShaderProgram;
import v2.modules.pbr.PBRShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

    private DeferredFBO deferredFBO;

    private TextureObject sceneTexture;
    private ShaderProgram lightingShader;

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
        lightingShader = new PBRDeferredShaderProgram();
        quad = new FSQuad();
        currTexture = deferredFBO.getAlbedo();
        sceneTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16, GL_RGBA)
                .bilinearFilter();
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



        if (InputCore.getInstance().isKeyPressed(GLFW_KEY_1)) {
            currTexture = deferredFBO.getAlbedo();
        } else if (InputCore.getInstance().isKeyPressed(GLFW_KEY_2)){
            currTexture = deferredFBO.getNormal();
        } else if (InputCore.getInstance().isKeyPressed(GLFW_KEY_3)){
            currTexture = deferredFBO.getMetalness();
        } else if (InputCore.getInstance().isKeyPressed(GLFW_KEY_4)){
            currTexture = deferredFBO.getRoughness();
        } else if (InputCore.getInstance().isKeyPressed(GLFW_KEY_5)){
            currTexture = deferredFBO.getDepth();
        } else if (InputCore.getInstance().isKeyPressed(GLFW_KEY_0)){
            currTexture = sceneTexture;
        }

        quad.setScreenTexture(currTexture);
        quad.render();


    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
