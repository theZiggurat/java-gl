package engine.scene.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import engine.glapi.VertexBufferObject;
import engine.glapi.Shader;

@Getter @AllArgsConstructor
public class RenderModule extends Module {

    /*
        Module with purpose of connecting OpenGL glapi calls to
        scenegraph nodes. Any node with this module will be able to
        attach a shader and VBO model.
     */

    private Shader shader;
    private VertexBufferObject mesh;

    @Override
    public void render() {

        shader.bind();
        shader.updateUniforms(getParent());
        mesh.render();
        shader.unbind();

    }

    @Override
    public void cleanup() {

        mesh.cleanup();
        shader.cleanup();

    }

}
