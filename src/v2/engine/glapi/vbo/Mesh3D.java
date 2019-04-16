package v2.engine.glapi.vbo;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import v2.engine.utils.Buffer;

import java.util.ArrayList;

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

public class Mesh3D extends VertexBufferObject {

    @Getter @Setter private ArrayList<Vector3f> positions;
    @Getter @Setter private ArrayList<Vector3f> normals;
    @Getter @Setter private ArrayList<Vector2f> UVs;
    @Getter @Setter private ArrayList<Integer> indices;


    public Mesh3D(){

        super();

        positions = new ArrayList<>();
        normals = new ArrayList<>();
        UVs = new ArrayList<>();
        indices = new ArrayList<>();

    }

    @Override
    public void bind(){

        glBindVertexArray(vaoId);

        glBindVertexArray(vaoId);
        vbos.stream().forEach(i ->glDeleteBuffers(i));
        vbos.clear();

        // position vbo
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Buffer.buffer3f(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);


        // uv vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Buffer.buffer2f(UVs), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0,0);

        // normal vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Buffer.buffer3f(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(2,3,GL_FLOAT, false, 0,0);

        // index vbo
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffer.indiciesBuffer(indices), GL_STATIC_DRAW);

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

    public float getLowest(){
        float ret = Float.MAX_VALUE;
        for(Vector3f pos: getPositions()){
            if(pos.y < ret) ret = pos.y;
        }
        return ret;
    }

    public int getVertexCount(){
        return indices.size();
    }



}
