package v2.engine.utils;

import org.lwjgl.BufferUtils;
import v2.engine.glapi.tex.TextureObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL21.GL_SRGB8;
import static org.lwjgl.opengl.GL21.GL_SRGB8_ALPHA8;
import static org.lwjgl.opengl.GL30.GL_RGB16F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.stb.STBImage.*;

public class ImageLoader {
    /**
     * Utility for loading image bytebuffers.
     * @param filename image path with format "res/image/*"
     * @return flipped byte glapi with image data
     */
    public static ByteBuffer loadImage(String filename){

        ByteBuffer buffer;

        try{
            buffer = Utils.ioResourceToBuffer(filename, 128*128);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        if(!stbi_info_from_memory(buffer, w, h, c)){
            throw new RuntimeException("Failed to read image info: " + stbi_failure_reason());
        }

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        System.out.println("Image loaded :" + filename + "(" + w.get(0) + "," + h.get(0) + ")");

        return image;
    }

    /**
     * Utility for loading texture to openGL directly
     * @param filename image path with format "res/images/*"
     * @return texture object with width, height, and handle
     */
    public static TextureObject loadTexture(String filename, boolean srgb){

        ByteBuffer buffer;

        try{
            buffer = Utils.ioResourceToBuffer(filename, 128*128);
        } catch(IOException e){
            return TextureObject.emptyTexture();
        } catch(NullPointerException e){
            return TextureObject.emptyTexture();
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        if(!stbi_info_from_memory(buffer, w, h, c)){
            throw new RuntimeException("Failed to read image info: " + stbi_failure_reason());
        }

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        int id = glGenTextures();
        TextureObject ret = new TextureObject(GL_TEXTURE_2D, w.get(0), h.get(0), id);
        ret.bilinearFilter();
        if (c.get(0) == 3) {
            if ((w.get(0) & 3) != 0) {
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w.get(0) & 1));
            }
            if(srgb)
                ret.allocateImage2D(GL_SRGB8, GL_RGB, image);
            else
                ret.allocateImage2D(GL_RGB16F, GL_RGB, image);
        } else if (c.get(0) == 1){
            ret.allocateImage2D(GL_RED, GL_RED, image);
        } else {
            if(srgb)
                ret.allocateImage2D(GL_SRGB8_ALPHA8, GL_RGBA, image);
            else
                ret.allocateImage2D(GL_RGBA16F, GL_RGBA, image);
    }

        stbi_image_free(image);
        System.out.println("Texture " + id + " loaded: " + filename + " (" + w.get(0) + "," + h.get(0) + ")");

        return ret;
    }
}
