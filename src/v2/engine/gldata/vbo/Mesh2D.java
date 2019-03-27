package v2.engine.gldata.vbo;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

//public class Mesh2D extends VertexBufferObject{

//    @Getter
//    private ArrayList<Vector2f> positions;
//
//    public Mesh2D(){
//        super();
//        positions = new ArrayList<>();
//    }
//
//    @Override
//    void bind() {
//
//    }
//
//    @Override
//    public void render() {
//        glBindVertexArray(vaoId);
//
//        glEnableVertexAttribArray(0); // pos
//        glDrawElements(GL_TRIANGLE_STRIP, getVertexCount(), GL_UNSIGNED_INT, 0);
//        glDisableVertexAttribArray(0);
//
//        glBindVertexArray(0);
//    }
//
//    @Override
//    int getVertexCount() {
//        return 0;
//    }


//}
