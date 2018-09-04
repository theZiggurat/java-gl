package v2.engine.system;

import org.lwjgl.BufferUtils;
import v2.engine.gldata.TextureObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;

public class StaticLoader {

    /**
     * Utility for loading image bytebuffers.
     * @param filename image path with format "res/image/*"
     * @return flipped byte gldata with image data
     */
    public static ByteBuffer loadImage(String filename){

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
        };

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        System.out.println("Image loaded :" + filename + "(" + w.get(0) + "," + h.get(0) + ")");

        return image;
    }

    /**
     * Utility for loading texture to openGL directly
     * @param filename image path with format "res/image/*"
     * @return texture object with width, height, and handle
     */
    public static TextureObject loadTexture(String filename){

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

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        if (c.get(0) == 3) {
            if ((w.get(0) & 3) != 0) {
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w.get(0) & 1));
            }
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(0), h.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }

        stbi_image_free(image);

        TextureObject ret = new TextureObject(GL_TEXTURE_2D, w.get(0), h.get(0), id);

        System.out.println("Texture " + id + " loaded: " + filename + " (" + w.get(0) + "," + h.get(0) + ")");

        return ret;
    }

    /**
     * Utility for loading string resources
     * @param filePath image path with format "res/*"
     * @return String resource
     * @throws Exception if path is not found
     */
    public static String loadResource(String filePath) throws Exception {
        String result;

        try (
            InputStream in = StaticLoader.class.getResourceAsStream("/"+filePath);
            Scanner scanner = new Scanner(in, "UTF-8"))

        { result = scanner.useDelimiter("\\A").next(); }


        System.out.println("Resouce loaded: " + filePath);
        return result;
    }

    /**
     * `
     * @param fileName  path with format "res/*"
     * @return List of strings that compose the file
     * @throws Exception file not found
     */
    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        InputStream is = StaticLoader.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader in = new InputStreamReader(is);
        try (BufferedReader br = new BufferedReader(in)) {
            String line;
            while ((line = br.readLine()) != null) { list.add(line); }
        }
        return list;
    }

    private static ByteBuffer ioResourceToBuffer(String filename, int bufferSize) throws IOException {

        ByteBuffer buffer;
        Path path = Paths.get(filename);

        if(Files.isReadable(path)){
            try(SeekableByteChannel channel = Files.newByteChannel(path)){
                buffer = BufferUtils.createByteBuffer((int)channel.size()+1);
            }
        }
        else {
            try (
                InputStream is = StaticLoader.class.getClassLoader().getResourceAsStream(filename);
                ReadableByteChannel rbc = Channels.newChannel(is)
            ){
                buffer = BufferUtils.createByteBuffer(bufferSize);
                while (true) {
                    int bytes = rbc.read(buffer);

                    if (bytes == -1)
                        break;
                    if (buffer.remaining() == 0)
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);

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
