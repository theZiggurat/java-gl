package v2.modules.debug;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import v2.engine.gldata.FrameBufferObject;
import v2.engine.gldata.TextureObject;
import v2.engine.system.Window;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OverlayFrameBufferObject extends FrameBufferObject {

    @Getter
    TextureObject overlay;

    public OverlayFrameBufferObject(){

        super();
        overlay = new TextureObject(GL_TEXTURE_2D,
                Window.instance().getWidth(), Window.instance().getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        IntBuffer drawBuffers = BufferUtils.createIntBuffer(1);
        drawBuffers.put(GL_COLOR_ATTACHMENT0);
        drawBuffers.flip();

        bind();
        createColorTextureAttachment(overlay.getId(), 0);
        setDrawBuffer(drawBuffers);
        checkStatus();
        unbind();
    }
}
