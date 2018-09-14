package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Scenegraph;
import v2.modules.pbr.PBRFrameBufferObject;
import v2.engine.quad.FSQuad;
import v2.modules.pbr.PBRDeferredShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

    private PBRFrameBufferObject PBRFrameBufferObject;

    private TextureObject sceneTexture;
    private PBRDeferredShaderProgram lightingPass;

    private FSQuad quad;

    @Setter @Getter private Camera mainCamera;

    public static Vector3f lightDir;

    public static RenderEngine instance = null;

    public static RenderEngine instance(){
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
        Window window = Window.instance();
        scenegraph.update();
        PBRFrameBufferObject = new PBRFrameBufferObject(window.getWidth(), window.getHeight(),1);
        lightingPass = new PBRDeferredShaderProgram();
        quad = new FSQuad();
        currTexture = PBRFrameBufferObject.getAlbedo();
        sceneTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        lightDir = new Vector3f(1,0,0);
    }

    TextureObject currTexture;

    int time;

    public void render(){

        time ++;

        lightDir = new Vector3f((float)Math.sin((double)time/1440), 0, (float)Math.cos((double)time/1440));

        mainCamera.update();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        PBRFrameBufferObject.bind();
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        PBRFrameBufferObject.unbind();

        if(Window.instance().isResized()){
            glViewport(0,0, Window.instance().getWidth(),
                    Window.instance().getHeight());
            Window.instance().setResized(false);
        }



        PBRFrameBufferObject.bind();
        if(InputCore.instance().isKeyHeld(GLFW_KEY_DELETE)){
            scenegraph.renderWireframe();
        } else {
            scenegraph.render();
        }
        PBRFrameBufferObject.unbind();

        lightingPass.render(PBRFrameBufferObject.getAlbedo(),
                PBRFrameBufferObject.getPosition(), PBRFrameBufferObject.getNormal(),
                PBRFrameBufferObject.getMetalness(), PBRFrameBufferObject.getRoughness(),
                PBRFrameBufferObject.getDepth(), sceneTexture, lightDir);



        if (InputCore.instance().isKeyPressed(GLFW_KEY_2)) {
            currTexture = PBRFrameBufferObject.getAlbedo();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_3)){
            currTexture = PBRFrameBufferObject.getNormal();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_4)){
            currTexture = PBRFrameBufferObject.getMetalness();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_5)){
            currTexture = PBRFrameBufferObject.getRoughness();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_7)){
            currTexture = PBRFrameBufferObject.getDepth();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_1)){
            currTexture = sceneTexture;
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_6)){
            currTexture = PBRFrameBufferObject.getPosition();
        }

        quad.setScreenTexture(currTexture);
        quad.render();


    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
