package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Scenegraph;
import v2.modules.pbr.PBRFrameBufferObject;
import v2.engine.quad.FSQuad;
import v2.modules.pbr.PBRDeferredShaderProgram;
import v2.modules.post.ssao.SSAOShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class RenderEngine {

    @Getter private Scenegraph scenegraph;

    private PBRFrameBufferObject pbrFBO;

    private TextureObject sceneTexture;
    private PBRDeferredShaderProgram lightingPass;
    private SSAOShaderProgram ssaoPass;

    private FSQuad quad;

    @Setter @Getter private Camera mainCamera;

    @Getter @Setter private Vector3f lightDir;

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
        pbrFBO = new PBRFrameBufferObject(window.getWidth(), window.getHeight(),1);
        lightingPass = new PBRDeferredShaderProgram();
        ssaoPass = new SSAOShaderProgram();
        quad = new FSQuad();
        sceneTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        currTexture = sceneTexture;
        lightDir = new Vector3f(1,0,0);
    }

    TextureObject currTexture;

    public void render(){

        mainCamera.update();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        pbrFBO.bind();
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        pbrFBO.unbind();

        if(Window.instance().isResized()){
            glViewport(0,0, Window.instance().getWidth(),
                    Window.instance().getHeight());
            Window.instance().setResized(false);
        }



        pbrFBO.bind();
        if(InputCore.instance().isKeyHeld(GLFW_KEY_DELETE)){
            scenegraph.renderWireframe();
        } else {
            scenegraph.render();
        }
        pbrFBO.unbind();

        ssaoPass.compute(pbrFBO.getPosition(), pbrFBO.getNormal());



        lightingPass.compute(pbrFBO.getAlbedo(),
                pbrFBO.getPosition(), pbrFBO.getNormal(),
                pbrFBO.getMetalness(), pbrFBO.getRoughness(),
                ssaoPass.getTargetTexture(), sceneTexture, lightDir);



        if (InputCore.instance().isKeyPressed(GLFW_KEY_2)) {
            currTexture = pbrFBO.getAlbedo();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_3)){
            currTexture = pbrFBO.getNormal();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_4)){
            currTexture = pbrFBO.getMetalness();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_5)){
            currTexture = pbrFBO.getRoughness();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_7)){
            currTexture = pbrFBO.getDepth();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_1)){
            currTexture = sceneTexture;
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_6)){
            currTexture = pbrFBO.getPosition();
        } else if (InputCore.instance().isKeyPressed(GLFW_KEY_8)){
            currTexture = ssaoPass.getTargetTexture();
        }

        quad.setScreenTexture(currTexture);
        quad.render();


    }

    public void cleanup(){
        scenegraph.cleanup();
    }


}
