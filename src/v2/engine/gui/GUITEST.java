package v2.engine.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.gldata.vbo.VertexBufferObject;
import v2.engine.event.Input;
import v2.engine.system.Shader;
import v2.engine.utils.Coordinates;

import static org.lwjgl.opengl.GL11.*;

public class GUITEST {

    private GUITESTShader shader;
    private VertexBufferObject quad;

    private Vector3f color;
    private Box box;

    public GUITEST(float x, float y, float width, float height){

        shader = new GUITESTShader();
        quad = Meshs.posquad;
        box = new Box(x, y, width, height);
        color = new Vector3f(1);


    }

    public void render(){

        if(Coordinates.insideBox(box, Input.instance().getCursorPos()))
            color = new Vector3f(1,0,0);
        else
            color = new Vector3f(1);

        glDisable(GL_DEPTH_TEST);

        shader.bind();
        shader.updateUniforms(this);
        quad.render();
        shader.unbind();

        glEnable(GL_DEPTH_TEST);
    }

    public class GUITESTShader extends Shader {

        private GUITESTShader(){
            super();
            createVertexShader("res/shaders/gui/GUITEST_vert.glsl");
            createFragmentShader("res/shaders/gui/GUITEST_frag.glsl");
            link();

            addUniform("scale");
            addUniform("position");
            addUniform("color");

        }

        public void updateUniforms(GUITEST gui) {
            setUniform("position", new Vector2f(box.getX(), box.getY()));
            setUniform("scale", new Vector2f(box.getWidth(), box.getHeight()));
            setUniform("color", color);
        }
    }
}
