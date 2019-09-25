package engine.glapi.fbo;

import engine.glapi.FrameBufferObject;
import lombok.Getter;
import engine.glapi.TextureObject;
import engine.system.Pipeline;

public class PipelineFrameBufferObject extends FrameBufferObject {

    @Getter private TextureObject scene;
    @Getter private TextureObject depth;

    public PipelineFrameBufferObject(Pipeline pipeline, boolean standalone){

        super();

        this.scene = pipeline.getSceneBuffer();
        this.depth = pipeline.getDepthBuffer();

        if(standalone) addAttatchments(scene, depth);
        checkStatus();
    }
}
