package v2.engine.scene;

import lombok.AllArgsConstructor;
import lombok.Getter;
import v2.engine.render.MeshVBO;
import v2.engine.render.ShaderProgram;

@Getter @AllArgsConstructor
public class RenderModule extends Module {

    /*
        Module with purpose of connecting OpenGL render calls to
        scenegraph nodes. Any node with this module will be able to
        attatch a shader and VBO model.
     */

    private ShaderProgram shader;
    private MeshVBO mesh;

    @Override
    public void render() {

        shader.bind();
        shader.updateUniforms(getParent());
        //mesh.render();
        shader.unbind();

    }

}
