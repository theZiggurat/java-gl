package v2.engine.gldata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import v2.engine.system.StaticLoader;


import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Getter @AllArgsConstructor
public class TextureObject {

    private int type, width, height, id;

    public TextureObject(int type, int width, int height){
        this(type, width, height, glGenTextures());
    }

    public TextureObject(int width, int height){
        this(GL_TEXTURE_2D, width, height);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup(){
        unbind();
        glDeleteTextures(id);
    }

    public TextureObject wrap(){
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        unbind();
        return this;
    }

    public TextureObject nofilter(){
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        unbind();
        return this;
    }

    public TextureObject bilinearFilter(){
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        unbind();
        return this;
    }

    public TextureObject trilinearFilter(){
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        unbind();
        return this;
    }

    public TextureObject allocateImage2D(int internalFormat, int format){
        bind();
        glTexImage2D(type, 0, internalFormat, width, height,
                0, format, GL_FLOAT, (ByteBuffer) null);
        unbind();
        return this;
    }

    public TextureObject allocateImage2D(int internalFormat, int format, ByteBuffer buffer){
        bind();
        glTexImage2D(type, 0, internalFormat, width, height,
                0, format, GL_UNSIGNED_BYTE, buffer);
        unbind();
        return this;
    }

    public TextureObject allocateDepth(){
        bind();
        glTexImage2D(type, 0, GL_DEPTH_COMPONENT32, width, height,
                0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        unbind();
        return this;
    }

    public static TextureObject emptyTexture(){
        return new TextureObject(GL_TEXTURE_2D,0,0,0);
    }

}
