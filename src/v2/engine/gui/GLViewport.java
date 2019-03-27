package v2.engine.gui;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.system.Shader;


import static org.lwjgl.opengl.GL11.*;

public class GLViewport extends Element {

    @Setter @Getter private TextureObject screenTexture;
    private Shader fsShader;

    public GLViewport() {
        super();
        fsShader = new GLViewportShader();
    }

    @Override
    public void render(){

        super.render();

        glPolygonMode(GL_FRONT, GL_FILL);

        fsShader.bind();
        fsShader.updateUniforms(screenTexture);
        Meshs.quad.render();
        fsShader.unbind();
    }

    @Override
    public void handle(){
        super.handle();
    }

    @Override
    public void cleanup(){
        super.cleanup();
        fsShader.cleanup();
    }

    private class GLViewportShader extends Shader {

        public GLViewportShader(){
            super();

            createVertexShader("res/shaders/viewport/viewport_vs.glsl");
            createFragmentShader("res/shaders/viewport/viewport_fs.glsl");
            link();

            addUniform("texture");
        }

        @Override
        public void updateUniforms(TextureObject textureObject){
            activeTexture(0);
            textureObject.bind();
            setUniform("texture", 0);
        }
    }

}
