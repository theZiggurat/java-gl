package v2.engine.gldata.vbo;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public interface VertexBufferObject {

    void bind();
    void render();
    int getVertexCount();
    void cleanup();

}
