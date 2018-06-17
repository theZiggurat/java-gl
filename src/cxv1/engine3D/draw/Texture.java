package cxv1.engine3D.draw;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int id;
    private int width, height;

    public Texture(String fileName) throws Exception {

        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(
                "/res/textures/"+fileName));

        this.width = decoder.getWidth();
        this.height = decoder.getHeight();

        // 4 bytes per pixel
        ByteBuffer buffer = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth()*4, Format.RGBA);
        buffer.flip();

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glGenerateMipmap(GL_TEXTURE_2D);
        this.id = id;
    }

    public Texture(int id){
        this.id = id;
    }

    public void cleanup(){
        glDeleteTextures(id);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void bind(){glBindTexture(GL_TEXTURE_2D, id);}
    public int getId(){return id;}
}
