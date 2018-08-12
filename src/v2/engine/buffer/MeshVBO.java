package v2.engine.buffer;

import v2.engine.mesh.MeshData;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MeshVBO {

    private List<Integer> vbos;
    private int vaoId;
    private int vertexCount;

    public MeshVBO(MeshData meshData){

        vertexCount = meshData.getIndices().length;
        vbos = new ArrayList<>();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // pos vbo
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, meshData.posBuffer(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // texture vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, meshData.uvBuffer(), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

        // index vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, meshData.indexBuffer(), GL_STATIC_DRAW);

        // normal vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, meshData.normalBuffer(), GL_STATIC_DRAW);
        glVertexAttribPointer(2,3,GL_FLOAT, false, 0,0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    public void render(){

        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0); // pos
        glEnableVertexAttribArray(1); // uv
        glEnableVertexAttribArray(2); // norm

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

    }

    public void cleanup(){
        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        glDeleteVertexArrays(vaoId);
        glBindVertexArray(0);
    }


}
