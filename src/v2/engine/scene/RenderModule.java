package v2.engine.scene;

import lombok.AllArgsConstructor;
import lombok.Getter;
import v2.engine.gldata.VertexBufferObject;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;

@Getter @AllArgsConstructor
public class RenderModule extends Module {

    /*
        Module with purpose of connecting OpenGL gldata calls to
        scenegraph nodes. Any node with this module will be able to
        attach a shader and VBO model.
     */

    private ShaderProgram shader;
    private VertexBufferObject mesh;

    @Override
    public void render() {

        shader.bind();
        shader.updateUniforms(getParent());
        mesh.render();
        shader.unbind();

    }

    @Override
    public void renderWireframe() {

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        shader.bind();
        shader.updateUniforms(getParent());
        mesh.render();
        shader.unbind();

        glPolygonMode(GL_FRONT_FACE, GL_FILL);

    }

    public void cleanup() {

        mesh.cleanup();
        shader.cleanup();

    }

}
