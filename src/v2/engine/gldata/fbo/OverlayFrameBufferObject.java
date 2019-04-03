package v2.engine.gldata.fbo;

import lombok.Getter;
import v2.engine.gldata.tex.TextureObject;
import v2.engine.system.Window;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OverlayFrameBufferObject extends FrameBufferObject {

    @Getter
    TextureObject overlay, depth;

    public OverlayFrameBufferObject(){

        super();
        overlay = new TextureObject(GL_TEXTURE_2D,
                Window.instance().getWidth(), Window.instance().getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        depth = new TextureObject(
                GL_TEXTURE_2D, Window.instance().getWidth(), Window.instance().getHeight())
                .allocateDepth()
                .bilinearFilter();

        addAttatchments(overlay, depth);
        checkStatus();
    }

    public void resize(int x, int y){
        overlay.resize(x,y);
        depth.resize(x,y);
    }
}
