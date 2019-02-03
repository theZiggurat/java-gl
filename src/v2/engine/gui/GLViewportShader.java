package v2.engine.gui;

import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;

public class GLViewportShader extends ShaderProgram {

    public GLViewportShader(){
        super();

        createVertexShader("res/shaders/fullscreen_vertex.vs");
        createFragmentShader("res/shaders/fullscreen_fragment.fs");
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
