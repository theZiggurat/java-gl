package cxv1.engine3D.draw.mesh;

import cxv1.engine3D.util.ShaderUtil;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public interface Mesh {

    void render(ShaderUtil shader);
    List<Integer> getVboList();
    int getVaoId();

    default void cleanup(){
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        // delete buffers and texture
        for(int i: getVboList()){glDeleteBuffers(i);}

        //materialManager.cleanup();

        // delete vao
        glBindVertexArray(0);
        glDeleteVertexArrays(getVaoId());
    }
}
