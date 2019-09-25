package modules.outline;

import engine.glapi.FrameBufferObject;
import lombok.Getter;
import engine.glapi.TextureObject;
import engine.system.Pipeline;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OutlineFrameBufferObject extends FrameBufferObject {

    @Getter
    private TextureObject colorBuffer;
    @Getter private TextureObject depthBuffer;

    public OutlineFrameBufferObject(Pipeline pipeline){

        super();

        colorBuffer = new TextureObject(GL_TEXTURE_2D, pipeline.getResolution())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        depthBuffer = new TextureObject(GL_TEXTURE_2D, pipeline.getResolution())
                .allocateDepth()
                .bilinearFilter();

        addAttatchments(colorBuffer, depthBuffer);
        checkStatus();
    }
}
