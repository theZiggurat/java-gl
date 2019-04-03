package v2.engine.gldata.vbo;

import lombok.Setter;
import org.joml.Vector2f;
import v2.engine.gui.Box;
import v2.engine.utils.Buffer;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh2D extends VertexBufferObject{

    @Setter private ArrayList<Vector2f> UVs;
    @Setter private ArrayList<Vector2f> positions;

    public Mesh2D(){
        super();
        positions = new ArrayList<>();
        UVs = new ArrayList<>();
    }

    @Override
    public void bind() {
        glBindVertexArray(vaoId);

        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        vbos.clear();

        // position vbo
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Buffer.buffer2f(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);


        // uv vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Buffer.buffer2f(UVs), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0); // pos
        glDrawElements(GL_TRIANGLE_STRIP, getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    @Override
    int getVertexCount() {
        return 0;
    }


}
