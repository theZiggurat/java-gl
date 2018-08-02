package cxv1.engine3D.util.loaders;

import cxv1.engine3D.draw.Texture;
import de.matthiasmann.twl.utils.PNGDecoder;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureLoader {

    public static Texture loadTexture(String fileName) throws Exception {
        switch (fileName.substring(fileName.indexOf('.')+1)){
            case "png":
                return loadPNG(fileName);
            case "tga":
                return loadTGA(fileName);
            default:
                throw new Exception(fileName.substring(fileName.indexOf('.')+1)+
                        " is an unsupported file type!");
        }
    }

    public static Texture loadPNG(String fileName) throws Exception {

        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(
                "/res/textures/"+fileName));

        int width = decoder.getWidth();
        int height = decoder.getHeight();

        // 4 bytes per pixel
        ByteBuffer buffer = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
        buffer.flip();

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glGenerateMipmap(GL_TEXTURE_2D);

        Texture texture = new Texture(id);
        texture.setHeight(height);
        texture.setWidth(width);

        return texture;
    }

    public static Texture loadTGA(String fileName) throws Exception{

        FileInputStream fis = new FileInputStream(new File(
                "/res/textures/"+fileName));
        byte [] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        int [] pixels = TGAReader.read(buffer, TGAReader.ABGR);
        int width = TGAReader.getWidth(buffer);
        int height = TGAReader.getHeight(buffer);

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_INT, pixels);

        glGenerateMipmap(GL_TEXTURE_2D);

        Texture texture = new Texture(id);
        texture.setWidth(width);
        texture.setHeight(height);

        return texture;
    }
}
