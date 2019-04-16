package v2.engine.glapi.fbo;

import lombok.Getter;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Config;
import v2.engine.system.Pipeline;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

public class PipelineFrameBufferObject extends FrameBufferObject {

    @Getter private TextureObject sceneBuffer;
    @Getter private TextureObject depthBuffer;

    public PipelineFrameBufferObject(Pipeline pipeline, boolean standalone){

        super();



        this.sceneBuffer = pipeline.getSceneBuffer();
        this.depthBuffer = pipeline.getDepthBuffer();

        if(standalone) addAttatchments(sceneBuffer, depthBuffer);
        checkStatus();
    }
}
