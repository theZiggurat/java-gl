package v2.engine.gui;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.vbo.VertexBufferObject;
import v2.engine.system.ShaderProgram;
import v2.engine.system.StaticLoader;


import static org.lwjgl.opengl.GL11.*;

public class GLViewport {

    @Setter @Getter private TextureObject screenTexture;
    private ShaderProgram fsShader;
    private VertexBufferObject quad;

    public GLViewport() {
        fsShader = new GLViewportShader();
        quad = StaticLoader.loadMeshGroup("res/models/quad.obj").get(0);
    }

    public void render(){

        glPolygonMode(GL_FRONT, GL_FILL);

        fsShader.bind();
        fsShader.updateUniforms(screenTexture);
        quad.render();
        fsShader.unbind();
    }

    public void cleanup(){
        quad.cleanup();
        fsShader.cleanup();
    }
}
