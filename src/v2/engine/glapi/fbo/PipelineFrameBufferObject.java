package v2.engine.glapi.fbo;

import lombok.Getter;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Pipeline;

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
