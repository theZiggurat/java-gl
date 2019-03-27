package v2.modules.pbr;

import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.RenderType;
import v2.engine.scene.Scenegraph;
import v2.engine.system.*;
import v2.modules.post.ssao.SSAOBlurShader;
import v2.modules.post.ssao.SSAOShader;
import v2.modules.shadow.ShadowFrameBufferObject;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

/**
 * This pipeline will be for realistic renderering with PBRModel objects
 * Using Cook-Torrance BDRF for physically based rendering
 */
public class PBRPipeline extends Pipeline {

    /**PBR FBO holds albedo, world pos,
     * world norm, roughness, metallic, and depth buffer */
    private PBRFrameBufferObject pbrFBO;

    /** Runs the compute shader to do lighting
     * calculation off of data in the pbrFBO */
    private PBRDeferredShader lightingPass;

    /** WIP SSAO shader pass */
    private SSAOShader ssaoPass;

    private SSAOBlurShader ssaoBlurPass;

    private ShadowFrameBufferObject shadowFBO;

    /** Output of PBR lighting pass compute shader */
    private TextureObject litTexture;

    private static PBRPipeline instance;
    public static PBRPipeline instance(){
        if(instance == null)
            instance = new PBRPipeline();
        return instance;
    }

    public PBRPipeline(){
        super();

        Window window = Window.instance();

        // create shaders and frame buffer
        pbrFBO = new PBRFrameBufferObject(window.getWidth(), window.getHeight(),1);
        lightingPass = new PBRDeferredShader();

        shadowFBO = new ShadowFrameBufferObject();

        ssaoPass = new SSAOShader();
        ssaoBlurPass = new SSAOBlurShader();

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
    protected void renderScene(Context context){

        if(Config.instance().isWireframe()){

            pbrFBO.bind(); {
                glPolygonMode( GL_FRONT_AND_BACK, GL_POINT );
                glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                Config.instance().setWireframeColor(new Vector3f(0.2f, 0.8f, 0.2f));
                glLineWidth(3f);
                context.getScene().render(RenderType.TYPE_WIREFRAME);
                glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
            } pbrFBO.unbind();
            sceneTexture = pbrFBO.getAlbedo();

        } else {

            shadowFBO.bind(); {
                glDisable(GL_CULL_FACE);
                glViewport(0,0, Config.instance().getShadowBufferWidth(),
                        Config.instance().getShadowBufferHeight());
                glClear(GL_DEPTH_BUFFER_BIT);
                context.getScene().render(RenderType.TYPE_SHADOW);
                glEnable(GL_CULL_FACE);
            }
            shadowFBO.unbind();

            pbrFBO.bind(); {
                glViewport(0,0, Window.instance().getWidth(),
                        Window.instance().getHeight());
                glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                // render scenegraph to obtain geometry data in the pbrFBO buffers
                context.getScene().render(RenderType.TYPE_SCENE, e ->
                    !e.isSelected()
                );

                glFrontFace(GL_CW);
                glDepthFunc(GL_ALWAYS);
                Core.selectionManager().renderSelected(RenderType.TYPE_DEPTH);
                glFrontFace(GL_CCW);


                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                glDepthFunc(GL_NOTEQUAL);
                glLineWidth(2f);
                Config.instance().setWireframeColor(new Vector3f(.1f, 0.8f, 0.2f));
                context.getSelectionManager().renderSelected(RenderType.TYPE_WIREFRAME);
                glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
                glDepthFunc(GL_LESS);

            } pbrFBO.unbind();


            // use buffer data to compute SSAO
            //ssaoPass.compute(pbrFBO.getPosition(), pbrFBO.getNormal());
            //ssaoBlurPass.compute(ssaoPass.getTargetTexture());

            // using buffer data to compute lit color
            lightingPass.compute(pbrFBO.getAlbedo(),
                    pbrFBO.getPosition(), pbrFBO.getNormal(), shadowFBO.getDepth(), litTexture);

            if (Input.instance().isKeyPressed(GLFW_KEY_1)){
                sceneTexture = litTexture;
            } else if (Input.instance().isKeyPressed(GLFW_KEY_2)) {
                sceneTexture = pbrFBO.getAlbedo();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_3)){
                sceneTexture = pbrFBO.getNormal();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_6)){
                    sceneTexture = pbrFBO.getPosition();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_7)) {
                sceneTexture = shadowFBO.getDepth();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_8)){
                sceneTexture = ssaoPass.getTargetTexture();
            } else if (Input.instance().isKeyPressed(GLFW_KEY_9)) {
                sceneTexture = context.getPicking().updateMap();
            }

        }

    }
}
