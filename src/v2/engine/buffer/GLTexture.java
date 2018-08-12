package v2.engine.buffer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import v2.engine.system.StaticLoader;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class GLTexture {

    @Getter int width, height;
    @Getter int id;

    public GLTexture(int width, int height, int id){
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void delete(){
        glDeleteTextures(id);
    }
}
