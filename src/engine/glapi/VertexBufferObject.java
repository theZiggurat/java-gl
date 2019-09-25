package engine.glapi;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class VertexBufferObject {

    protected List<Integer> vbos;
    protected int vaoId;

    protected VertexBufferObject(){
        vbos = new ArrayList<>();
        vaoId = glGenVertexArrays();
    }

    public abstract void bind();
    public abstract void render();
    public abstract int getVertexCount();

    public void cleanup(){
        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        glDeleteVertexArrays(vaoId);
        glBindVertexArray(0);
    }

}
