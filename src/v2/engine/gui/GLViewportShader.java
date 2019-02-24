package v2.engine.gui;

import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;

public class GLViewportShader extends ShaderProgram {

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
