package v2.engine.gldata.tex;

import lombok.Getter;
import lombok.Setter;


import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

@Getter
public class TextureObject {

    private int type, id, internalFormat, format, dataType;
    private boolean isDepth = false;
    @Getter @Setter private int width, height;

    public TextureObject(int type, int width, int height, int id){
        this.type = type;
        this.width = width;
        this.height = height;
        this.id = id;
        this.dataType = GL_FLOAT;
        this.internalFormat = GL_RGBA16F;
        this.format = GL_RGBA;
    }

    public TextureObject(int type, int width, int height){
        this(type, width, height, glGenTextures());
    }

    public TextureObject(int width, int height){
        this(GL_TEXTURE_2D, width, height);
    }

    public void bind(){
        glBindTexture(type, id);
    }

    public void unbind(){
        glBindTexture(type, 0);
    }

    public void cleanup(){
        unbind();
        glDeleteTextures(id);
    }

    public TextureObject wrap(){
        bind();
        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        unbind();
        return this;
    }

    public TextureObject nofilter(){
        bind();
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        unbind();
        return this;
    }

    public TextureObject bilinearFilter(){
        bind();
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        unbind();
        return this;
    }

    public TextureObject trilinearFilter(){
        bind();
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glGenerateMipmap(type);
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        unbind();
        return this;
    }

    public TextureObject clampEdge(){
        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return this;
    }

    public TextureObject clampBorder(){
        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        return this;
    }

    public void repeat(){

        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    public void mirroredRepeat(){

        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
    }

    public TextureObject allocateImage2D(int internalFormat, int format){
        this.internalFormat = internalFormat;
        this.format = format;
        this.dataType = GL_FLOAT;
        allocate();
        return this;
    }

    public TextureObject allocateImage2D(int internalFormat, int format, ByteBuffer buffer){
        this.internalFormat = internalFormat;
        this.format = format;
        this.dataType = GL_UNSIGNED_BYTE;
        allocate(buffer);
        return this;
    }

    public TextureObject allocateDepth(){
        bind();
        this.internalFormat = GL_DEPTH_COMPONENT32;
        this.format = GL_DEPTH_COMPONENT;
        this.dataType = GL_FLOAT;
        allocate();
        unbind();
        isDepth = true;
        return this;
    }

    public void resize(int xSize, int ySize){
        this.width = xSize;
        this.height = ySize;
        allocate();
    }

    private void allocate(){
        bind();
        glTexImage2D(type, 0, internalFormat, width, height, 0,
                format, dataType, 0);
        unbind();
    }

    private void allocate(ByteBuffer buf){
        bind();
        glTexImage2D(type, 0, internalFormat, width, height, 0,
                format, dataType, buf);
        unbind();
    }

    public static TextureObject emptyTexture(){
        return new TextureObject(GL_TEXTURE_2D,0,0,0);
    }

    public boolean isDepth(){
        return isDepth;
    }

}
