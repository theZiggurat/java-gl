package v2.modules.deferred;

import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;

public class FSShaderProgram extends ShaderProgram {

    public FSShaderProgram(){
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
