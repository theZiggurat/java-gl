package v2.engine.system;

import lombok.Getter;
import org.joml.Vector2i;
import v2.engine.glapi.fbo.OutlineFrameBufferObject;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.scene.SceneContext;
import v2.engine.scene.node.RenderType;
import v2.engine.utils.Color;
import v2.modules.generic.OverlayBlendingShader;
import v2.engine.glapi.fbo.PipelineFrameBufferObject;
import v2.modules.generic.OutlineShader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

public abstract class Pipeline {

    @Getter private PipelineFrameBufferObject pipelineBuffer;
    @Getter private OutlineFrameBufferObject  outlineBuffer;

    @Getter private SceneContext context;

    @Getter TextureObject sceneBuffer;
    @Getter TextureObject depthBuffer;
    private OverlayBlendingShader overlayBlending;

    private final float OUTLINE_SCALE_FACTOR = 0.1f;

    /** TODO: refactor with camera controller **/

    /**
     * Base class of the pipeline which provides the following functionality:
     *  1. Overlay rendering (outlines, debug scene objects)
     *  2. Render target texture and depth target texture
     * @param context 3D context for pipleline to run for
     */
    protected Pipeline(SceneContext context){
        this.context = context;

        int target;
        if(Config.instance().getMultisamples() > 0) target = GL_TEXTURE_2D_MULTISAMPLE;
        else target = GL_TEXTURE_2D;

        overlayBlending = new OverlayBlendingShader(context);

        sceneBuffer = new TextureObject(target, getResolution())
                    .allocateImage2D(GL_RGBA16F, GL_RGBA)
                    .bilinearFilter();

        depthBuffer = new TextureObject(target, getResolution())
                    .allocateDepth()
                    .bilinearFilter();

        pipelineBuffer = new PipelineFrameBufferObject(this, true);
        outlineBuffer = new OutlineFrameBufferObject(this);


    }

    // explicitly update the resolution of pipeline fields
    public void resize(){
        pipelineBuffer.resize(getResolution());
        outlineBuffer.resize(getResolution());
    }

    public Vector2i getResolution(){
        return context.getResolution();
    }

    protected abstract void renderScene(SceneContext context);

    public void draw(){

        // call sub-render method
        // that will populate the scene texture

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT| GL_STENCIL_BUFFER_BIT);

        renderScene(context);

        if(Config.instance().isDebugLayer()) {

            outlineBuffer.bind(() -> {

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT| GL_STENCIL_BUFFER_BIT);
                Window.instance().resizeViewport(getResolution());

                OutlineShader.instance().setColor(new Color(0xe28c0b));
                OutlineShader.setOffset(OUTLINE_SCALE_FACTOR);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                context.getSelectionManager().renderSelected(RenderType.TYPE_OUTLINE);

                glPolygonMode(GL_FRONT, GL_FILL);
                OutlineShader.instance().setColor(new Color(0x0));
                OutlineShader.setOffset(0f);
                glDepthFunc(GL_ALWAYS);
                context.getSelectionManager().renderSelected(RenderType.TYPE_OUTLINE);
                Window.instance().resetViewport();
                glDepthFunc(GL_LESS);
            });

//            pipelineBuffer.bind(()-> {
//
//                Window.instance().resizeViewport(getResolution());
//
//                context.getScene().render(RenderType.TYPE_OVERLAY);
//                //glPolygonMode(GL_FRONT, GL_LINE);
//                //glDepthFunc(GL_LEQUAL);
//                glPolygonOffset(-1f, 0);
//                glLineWidth(2f);
//
//                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//                glPolygonOffset(0, 0);
//                //glDepthFunc(GL_LESS);
//
//                Window.instance().resetViewport();
//            });

            overlayBlending.compute(
                pipelineBuffer.getScene(), outlineBuffer.getColorBuffer()
            );


        }


}

}