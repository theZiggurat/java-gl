package v2.engine.glapi.tex;

import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Texture2D extends TextureObject {

    public Texture2D(Vector2i resolution){
        super(GL_TEXTURE_2D, resolution);
    }
}
