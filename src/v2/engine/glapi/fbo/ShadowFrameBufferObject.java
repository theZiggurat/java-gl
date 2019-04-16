package v2.engine.glapi.fbo;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Config;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

public class ShadowFrameBufferObject extends FrameBufferObject {

    @Getter private TextureObject depth;

    public ShadowFrameBufferObject(){
        super();
        depth = new TextureObject(
                GL_TEXTURE_2D, Config.instance().getShadowBufferWidth(),
                Config.instance().getShadowBufferHeight())
                .allocateDepth()
                .wrap()
                .nofilter();

        IntBuffer drawBuffers = BufferUtils.createIntBuffer(1);
        drawBuffers.put(GL_COLOR_ATTACHMENT0);
        drawBuffers.flip();

        bind();
        createDepthTextureAttatchment(depth.getId());
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        setDrawBuffer(drawBuffers);
        checkStatus();
        unbind();
    }

    public void resize(int x, int y){ }
}
