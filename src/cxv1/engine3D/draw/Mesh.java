package cxv1.engine3D.draw;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;

public class Mesh {

    String id;
    private int vaoId;
    private final List<Integer> vboList;
    private final int vertexCount;
    private Material material;

    public Mesh(String id, float[] positions, float[] textureCoords, int[] indices, float[] normals){
        this.id = id;
        FloatBuffer  posBuffer = null;
        FloatBuffer textureBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer normalBuffer = null;
        try{
            vertexCount = indices.length;
            vboList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // pos vbo
            int vbo = glGenBuffers();
            vboList.add(vbo);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // texture vbo
            vbo = glGenBuffers();
            vboList.add(vbo);
            textureBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            textureBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

            // index vbo 1
            vbo = glGenBuffers();
            vboList.add(vbo);
            indicesBuffer = MemoryUtil.memAllocInt(vertexCount);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // index vbo 2
            vbo = glGenBuffers();
            vboList.add(vbo);
            indicesBuffer = MemoryUtil.memAllocInt(vertexCount);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // normal vbo
            vbo = glGenBuffers();
            vboList.add(vbo);
            normalBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2,3,GL_FLOAT, false, 0,0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        } finally {
            if(posBuffer != null){ MemoryUtil.memFree(posBuffer); }
            if(indicesBuffer != null){ MemoryUtil.memFree(indicesBuffer); }
            if(textureBuffer != null){ MemoryUtil.memFree(textureBuffer); }
            if(normalBuffer != null){MemoryUtil.memFree(normalBuffer);}
        }
    }

    public void render(){

        Texture texture = material.getTexture();
        // activate texture
        if(texture!=null){
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        glBindVertexArray(getVaoId());
        // comment comment comment 
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount()/2, GL_UNSIGNED_INT, 0);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, getVertexCount()/2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup(){
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        // delete buffers and texture
        for(int i: vboList){glDeleteBuffers(i);}

        Texture texture = material.getTexture();
        if(texture!=null){ texture.cleanup();}

        // delete vao
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void deleteBuffers(){
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0 );
        for(int vboId: vboList){
            glDeleteBuffers(vboId);
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public int getVaoId() { return vaoId; }
    public int getVertexCount() { return vertexCount; }

    public Material getMaterial(){return material;}
    public void setMaterial(Material material){this.material = material;}

    public String getId(){ return id; }
}
