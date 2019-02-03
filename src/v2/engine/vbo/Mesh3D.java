package v2.engine.vbo;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import v2.engine.javadata.StaticBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh3D implements VertexBufferObject {

    @Getter
    @Setter private ArrayList<Vector3f> positions;
    @Getter @Setter private ArrayList<Vector3f> normals;
    @Getter @Setter private ArrayList<Vector2f> UVs;
    @Getter @Setter private ArrayList<Integer> indices;

    private List<Integer> vbos;
    private int vaoId;

    public Mesh3D(){

        positions = new ArrayList<>();
        normals = new ArrayList<>();
        UVs = new ArrayList<>();
        indices = new ArrayList<>();

        vbos = new ArrayList<>();
        vaoId = glGenVertexArrays();
    }

    public void bind(){

        glBindVertexArray(vaoId);

        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        vbos.clear();

        // position vbo
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, StaticBuffer.positionBuffer(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);


        // uv vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, StaticBuffer.UVBuffer(UVs), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

        // normal vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, StaticBuffer.normalBuffer(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(2,3,GL_FLOAT, false, 0,0);

        // index vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, StaticBuffer.indiciesBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(){

        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0); // pos
        glEnableVertexAttribArray(1); // uv
        glEnableVertexAttribArray(2); // norm

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

    }

    public int getVertexCount(){
        return indices.size();
    }

    public void cleanup(){
        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        glDeleteVertexArrays(vaoId);
        glBindVertexArray(0);
    }

}
