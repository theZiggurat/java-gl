package v2.modules.pbr;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.light.LightManager;
import v2.engine.system.Config;
import v2.engine.system.Input;
import v2.engine.system.RenderEngine;
import v2.engine.system.Window;
import v2.modules.post.ssao.SSAOBlurShaderProgram;
import v2.modules.post.ssao.SSAOShaderProgram;
import v2.modules.shadow.ShadowFrameBufferObject;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

/**
 * This renderer will be for realistic renderering with PBRModel objects
 * Using Cook-Torrance BDRF for physically based rendering
 */
public class PBRRenderEngine extends RenderEngine {

    /**PBR FBO holds albedo, world pos,
     * world norm, roughness, metallic, and depth buffer */
    private PBRFrameBufferObject pbrFBO;

    /** Runs the compute shader to do lighting
     * calculation off of data in the pbrFBO */
    private PBRDeferredShaderProgram lightingPass;

    /** WIP SSAO shader pass */
    private SSAOShaderProgram ssaoPass;

    private SSAOBlurShaderProgram ssaoBlurPass;

    private ShadowFrameBufferObject shadowFBO;

    /** Output of PBR lighting pass compute shader */
    private TextureObject litTexture;

    private static PBRRenderEngine instance;
    public static PBRRenderEngine instance(){
        if(instance == null)
            instance = new PBRRenderEngine();
        return instance;
    }

    public PBRRenderEngine(){
        super();

        Window window = Window.instance();

        // create shaders and frame buffer
        pbrFBO = new PBRFrameBufferObject(window.getWidth(), window.getHeight(),1);
        lightingPass = new PBRDeferredShaderProgram();

        shadowFBO = new ShadowFrameBufferObject();

        ssaoPass = new SSAOShaderProgram();
        ssaoBlurPass = new SSAOBlurShaderProgram();

        // RGBA texture that will be mapped to the full screen gui
        litTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        // we will initially draw the lit texture to the screen
        sceneTexture = litTexture;

        instance = this;
    }

    @Override
    public void onResize(){
        super.onResize();
        pbrFBO.resize(Window.instance().getWidth(), Window.instance().getHeight());
        litTexture.resize(Window.instance().getWidth(), Window.instance().getHeight());
    }

    @Override
    protected void renderCamera(){

        mainCamera.update();

        shadowFBO.bind();
        //glCullFace(GL_FRONT);
        glDisable(GL_CULL_FACE);
        glViewport(0,0, Config.instance().getShadowBufferWidth(),
                Config.instance().getShadowBufferHeight());
        glClear(GL_DEPTH_BUFFER_BIT);
        scenegraph.renderShadow();
        glEnable(GL_CULL_FACE);
        //glCullFace(GL_BACK);
        shadowFBO.unbind();


        // clear render buffers


        pbrFBO.bind();
        glViewport(0,0, Window.instance().getWidth(),
                Window.instance().getHeight());
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // render scenegraph to obtain geometry data in the pbrFBO buffers
        if(Input.instance().isKeyHeld(GLFW_KEY_DELETE)){
            scenegraph.renderWireframe();
        } else {
            scenegraph.render();
        }
        pbrFBO.unbind();

        // use buffer data to compute SSAO
        ssaoPass.compute(pbrFBO.getPosition(), pbrFBO.getNormal());
        ssaoBlurPass.compute(ssaoPass.getTargetTexture());

        // using buffer data to compute lit color
        lightingPass.compute(pbrFBO.getAlbedo(),
                pbrFBO.getPosition(), pbrFBO.getNormal(),
                pbrFBO.getMetalness(), pbrFBO.getRoughness(),
                ssaoBlurPass.getTargetTexture(), shadowFBO.getDepth(), litTexture);

        if (Input.instance().isKeyPressed(GLFW_KEY_7)) {
            sceneTexture = shadowFBO.getDepth();
        } else {
            glViewport(0,0,Window.instance().getWidth(), Window.instance().getHeight());
            // poll which texture to render
            if (Input.instance().isKeyPressed(GLFW_KEY_2)) {
                sceneTexture = pbrFBO.getAlbedo();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_3)){
                sceneTexture = pbrFBO.getNormal();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_4)){
                sceneTexture = pbrFBO.getMetalness();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_5)){
                sceneTexture = pbrFBO.getRoughness();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_1)){
                sceneTexture = litTexture;
            } else if (Input.instance().isKeyPressed(GLFW_KEY_6)){
                sceneTexture = pbrFBO.getPosition();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_8)){
                sceneTexture = ssaoPass.getTargetTexture();
            }
        }


    }
}
