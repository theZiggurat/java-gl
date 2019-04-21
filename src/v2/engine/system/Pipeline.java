package v2.engine.system;

import lombok.Getter;
import org.joml.Vector2i;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.scene.SceneContext;
import v2.engine.scene.node.RenderType;
import v2.modules.generic.OverlayBlendingShader;
import v2.engine.glapi.fbo.PipelineFrameBufferObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_ALPHA_TYPE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

public abstract class Pipeline {

    @Getter private PipelineFrameBufferObject pipelineBuffer;

    @Getter private SceneContext context;

    @Getter TextureObject sceneBuffer;
    @Getter TextureObject depthBuffer;

    /** TODO: refactor with camera controller **/

    /**
     * Base class of the pipeline which provides the following functionality:
     *  1. Overlay rendering (outlines, debug scene objects, )
     * @param context 3D context for pipleline to run for
     */
    protected Pipeline(SceneContext context){
        this.context = context;

        int target;
        if(Config.instance().getMultisamples() > 0) target = GL_TEXTURE_2D_MULTISAMPLE;
        else target = GL_TEXTURE_2D;

        sceneBuffer = new TextureObject(target, getResolution())
                    .allocateImage2D(GL_RGBA16F, GL_RGBA)
                    .bilinearFilter();

        depthBuffer = new TextureObject(target, getResolution())
                    .allocateDepth()
                    .bilinearFilter();

        pipelineBuffer = new PipelineFrameBufferObject(this, true);

    }

    // explicitly update the resolution of pipeline fields
    public void resize(){
        pipelineBuffer.resize(getResolution());
    }

    public Vector2i getResolution(){
        return context.getResolution();
    }

    protected abstract void renderScene(SceneContext context);

    public void draw(){

        // call sub-render method
        // that will populate the scene texture
        renderScene(context);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(Config.instance().isDebugLayer()) {

            pipelineBuffer.bind(()-> {

                context.getScene().render(RenderType.TYPE_OVERLAY);
                glPolygonMode(GL_FRONT, GL_LINE);
                glDepthFunc(GL_EQUAL);
                glLineWidth(1f);
                context.getSelectionManager().renderSelected(RenderType.TYPE_WIREFRAME);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                glDepthFunc(GL_LESS);
            });
        }
    }

}