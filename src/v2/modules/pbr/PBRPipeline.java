package v2.modules.pbr;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.glapi.fbo.PBRFrameBufferObject;
import v2.engine.scene.SceneContext;
import v2.engine.scene.node.RenderType;
import v2.engine.system.*;
import v2.engine.glapi.fbo.ShadowFrameBufferObject;
import v2.modules.post.bloom.Bloom;
import v2.modules.post.ssao.SSAO;
import v2.modules.post.ssr.SSR;
import v2.modules.post.tonemap.Tonemap;

import static org.lwjgl.opengl.GL11.*;

/**
 * This pipeline will be for realistic renderering with PBRModel objects
 * Using Cook-Torrance BDRF for physically based rendering
 */
public class PBRPipeline extends Pipeline {


    // render component buffer (albedo, position, normal, etc)
    @Getter private PBRFrameBufferObject pbrFBO;
    // fbo needed to calculate shadow factor for main lighting pass
    @Getter private ShadowFrameBufferObject shadowFBO;

    /** PASSES **/
    // main lighting pass
    @Getter private PBRDeferredShader lightingPass;
    // screen space ambient occlusion pass
    @Getter private SSAO ssaoPass;
    // screen space reflection pass
    @Getter private SSR ssrPass;
    // bloom pass
    @Getter private Bloom bloomPass;
    // tone mapping
    @Getter private Tonemap tonemap;

    public PBRPipeline(SceneContext context) {

        super(context);

        lightingPass = new PBRDeferredShader();
        pbrFBO = new PBRFrameBufferObject(this);
        shadowFBO = new ShadowFrameBufferObject();

        ssaoPass = new SSAO(this);
        ssrPass = new SSR(this);

        bloomPass = new Bloom();
        tonemap = new Tonemap();
    }

    @Override
    public void resize() {
        super.resize();
        pbrFBO.resize(getContext().getResolution());
        ssaoPass.resize();
    }

    @Override
    protected void renderScene(SceneContext context) {

        if(Config.instance().isShadows()) {
            shadowFBO.bind(() -> {
                glDisable(GL_CULL_FACE);
                glViewport(0, 0, Config.instance().getShadowBufferWidth(),
                    Config.instance().getShadowBufferHeight());
                glClear(GL_DEPTH_BUFFER_BIT);
                context.getScene().render(RenderType.TYPE_SHADOW);
                glEnable(GL_CULL_FACE);
            });
        }

        pbrFBO.bind(()-> {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT| GL_STENCIL_BUFFER_BIT);
            Window.instance().resizeViewport(context.getResolution());

            // render scenegraph to obtain geometry data in the pbrFBO buffers
            context.getScene().render(RenderType.TYPE_SCENE);
        });

        // calculate ssao
        if(Config.instance().isSsao())
            ssaoPass.compute(
            pbrFBO.getPosition(),
            pbrFBO.getNormal());

        // using buffer data to compute lit color
        lightingPass.compute(
            pbrFBO.getAlbedo(),
            pbrFBO.getPosition(),
            pbrFBO.getNormal(),
            shadowFBO.getDepth(),
            ssaoPass.getTargetTexture(),
            getSceneBuffer());

        // calculate reflections
        if(Config.instance().isSsr())
            ssrPass.compute(
            pbrFBO.getPosition(),
            pbrFBO.getNormal(),
            ssaoPass.getTargetTexture());

        pbrFBO.bind(() -> {
            context.getScene().getSky().render(RenderType.TYPE_SCENE);
        });

        // reset viewport to window size
        Window.instance().resetViewport();

    }
}
