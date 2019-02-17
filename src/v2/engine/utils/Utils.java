package v2.engine.utils;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {


    /**
     * Utility for loading string resources
     * @param filePath image path with format "res/*"
     * @return String resource
     * @throws Exception if path is not found
     */
    public static String loadResource(String filePath) throws Exception {
        String result;

        try (
                InputStream in = Utils.class.getResourceAsStream("/"+filePath);
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
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader in = new InputStreamReader(is);
        try (BufferedReader br = new BufferedReader(in)) {
            String line;
            while ((line = br.readLine()) != null) { list.add(line); }
        }
        return list;
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
                InputStream is = Utils.class.getClassLoader().getResourceAsStream(filename);
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

    public static String absolutePath(String relativePath){
        if(relativePath.charAt(0) == '/')
                relativePath = relativePath.substring(1);
        return Utils.class.getClassLoader().getResource(relativePath).getPath().substring(1)
                .replaceAll("out/production/CVX1.0%20FPSEngine", "src");
    }
}
