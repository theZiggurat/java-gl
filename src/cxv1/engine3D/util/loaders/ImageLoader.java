package cxv1.engine3D.util.loaders;

import cxv1.engine3D.draw.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengles.GLES20.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class ImageLoader {

    // Credit for this image-loader Fynn @OreonEngine

    public static Texture loadImage(String filename){

        ByteBuffer buffer;

        try{
            buffer = ioResourceToBuffer(filename, 128*128);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        if(!stbi_info_from_memory(buffer, w, h, c)){
            throw new RuntimeException("Failed to read image info: " + stbi_failure_reason());
        }

        System.out.println("Image width: " + w.get(0));
        System.out.println("Image height: " + h.get(0));
        System.out.println("Image components: " + c.get(0));
        System.out.println("Image HDR: " + stbi_is_hdr_from_memory(buffer));

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        int width = w.get(0);
        int height = h.get(0);
        int comp = c.get(0);

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL11.GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL11.GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        if(comp == 3){
            if((width&3)!=0){
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2-(width&1));
            }
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0,
                    GL_RGB, GL_UNSIGNED_BYTE, image);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, image);
        }


        //stbi_image_free(image);

        Texture texture = new Texture(id, width, height);

        return texture;
    }

    public static ByteBuffer ioResourceToBuffer(String filename, int bufferSize) throws IOException {

        ByteBuffer buffer;


        Path path = Paths.get("res/textures/"+filename);
        if(Files.isReadable(path)){
            try(SeekableByteChannel channel = Files.newByteChannel(path)){
                buffer = BufferUtils.createByteBuffer((int)channel.size()+1);
            }
        } else{
            try (
                InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream("res/textures/"+filename);
                ReadableByteChannel rbc = Channels.newChannel(is)
            ){
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while(true){
                    int bytes = rbc.read(buffer);
                    if(bytes == -1){ // end of stream
                        break;
                    }

                    if(buffer.remaining() == 0){
                        buffer = resizeBuffer(buffer, buffer.capacity()*2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity){
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
