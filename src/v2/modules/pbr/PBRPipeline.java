package v2.modules.pbr;

import lombok.Getter;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import v2.engine.glapi.fbo.PBRFrameBufferObject;
import v2.engine.scene.SceneContext;
import v2.engine.scene.node.RenderType;
import v2.engine.system.*;
import v2.engine.glapi.fbo.ShadowFrameBufferObject;
import v2.modules.post.ssao.SSAO;
import v2.modules.post.ssao.SSAOShader;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_BUFFERS;

/**
 * This pipeline will be for realistic renderering with PBRModel objects
 * Using Cook-Torrance BDRF for physically based rendering
 */
public class PBRPipeline extends Pipeline {

    // main lighting pass
    @Getter private PBRDeferredShader lightingPass;
    // render component buffer (albedo, position, normal, etc)
    @Getter private PBRFrameBufferObject pbrFBO;
    // fbo needed to calculate shadow factor for main lighting pass
    @Getter private ShadowFrameBufferObject shadowFBO;
    // screen space ambient occlusion pass
    @Getter private SSAO ssaoPass;

    public PBRPipeline(SceneContext context) {

        super(context);

        lightingPass = new PBRDeferredShader();
        pbrFBO = new PBRFrameBufferObject(this);
        shadowFBO = new ShadowFrameBufferObject();
        ssaoPass = new SSAO(this);

        pbrFBO.bind(() ->{
            IntBuffer ret = BufferUtils.createIntBuffer(16);
            glGetIntegerv(GL_SAMPLE_BUFFERS, ret);
            if(ret.get(0) == 1){
                System.out.println("HEYYYYYYYy");
            }
        });

    }

    @Override
    public void resize() {
        super.resize();
        pbrFBO.resize(getContext().getResolution());
        ssaoPass.resize();
    }

    @Override
    protected void renderScene(SceneContext context) {

        if (Config.instance().isWireframe()) {

            pbrFBO.bind();
            {
                Window.instance().resizeViewport(context.getResolution());
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                Config.instance().setWireframeColor(new Vector3f(0.2f, 0.8f, 0.2f));
                glLineWidth(1f);
                context.getScene().render(RenderType.TYPE_WIREFRAME);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                Window.instance().resetViewport();
            }
            pbrFBO.unbind();

        } else {

            if(Config.instance().isShadows()) {
                shadowFBO.bind(() -> {
                    glDisable(GL_CULL_FACE);
                    glViewport(0, 0, Config.instance().getShadowBufferWidth(),
                            Config.instance().getShadowBufferHeight());
                    glClear(GL_DEPTH_BUFFER_BIT);
                    context.getScene().render(RenderType.TYPE_SHADOW, e -> !e.isSelected());
                    glEnable(GL_CULL_FACE);
                });
            }

            pbrFBO.bind(()-> {
                Window.instance().resizeViewport(context.getResolution());
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // render scenegraph to obtain geometry data in the pbrFBO buffers
                context.getScene().render(RenderType.TYPE_SCENE, e -> !e.isSelected());
            });

            ssaoPass.compute(pbrFBO.getPosition(), pbrFBO.getNormal());

            // using buffer data to compute lit color
            lightingPass.compute(
                    pbrFBO.getAlbedo(),
                    pbrFBO.getPosition(),
                    pbrFBO.getNormal(),
                    shadowFBO.getDepth(),
                    ssaoPass.getTargetTexture(),
                    getSceneBuffer());

            Window.instance().resetViewport();
        }

    }
}
