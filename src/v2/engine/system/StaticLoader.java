package v2.engine.system;

import org.lwjgl.BufferUtils;

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
import java.util.Scanner;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;

public class StaticLoader {

    /**
     * Utility for loading image bytebuffers.
     * @param filename image path with format "res/image/*"
     * @return flipped byte buffer with image data
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
        }

        System.out.println("Image name: " + filename);
        System.out.println("Image width: " + w.get(0));
        System.out.println("Image height: " + h.get(0));
        System.out.println("Image components: " + c.get(0));
        System.out.println("Image HDR: " + stbi_is_hdr_from_memory(buffer));

        ByteBuffer image = stbi_load_from_memory(buffer, w, h, c, 0);

        if(image == null){
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        return image;
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
            InputStream in = StaticLoader.class.getResourceAsStream(filePath);
            Scanner scanner = new Scanner(in, "UTF-8"))

        { result = scanner.useDelimiter("\\A").next(); }

        return result;
    }

    public static ByteBuffer ioResourceToBuffer(String filename, int bufferSize) throws IOException {

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
