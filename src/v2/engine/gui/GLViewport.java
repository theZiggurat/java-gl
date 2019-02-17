package v2.engine.gui;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.system.ShaderProgram;


import static org.lwjgl.opengl.GL11.*;

public class GLViewport {

    @Setter @Getter private TextureObject screenTexture;
    private ShaderProgram fsShader;

    public GLViewport() {
        fsShader = new GLViewportShader();
    }

    public void render(){

        glPolygonMode(GL_FRONT, GL_FILL);

        fsShader.bind();
        fsShader.updateUniforms(screenTexture);
        Meshs.quad.render();
        fsShader.unbind();
    }

    public void cleanup(){
        fsShader.cleanup();
    }
}
