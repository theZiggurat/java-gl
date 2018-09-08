package v2.modules.pbr;

import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL13.*;

public class PBRDeferredShaderProgram extends ShaderProgram {

    public PBRDeferredShaderProgram(){

    }

    public void updateUniforms(TextureObject albedo, TextureObject position, TextureObject normal,
       TextureObject metal, TextureObject rough, TextureObject depth, TextureObject scene){

        glActiveTexture(GL_TEXTURE0);
        position.bind();
        setUniform("positionMap", 0);

        glActiveTexture(GL_TEXTURE1);
        normal.bind();
        setUniform("normalMap", 1);

        glActiveTexture(GL_TEXTURE2);
        rough.bind();
        setUniform("roughnessMap", 2);

        glActiveTexture(GL_TEXTURE3);
        metal.bind();
        setUniform("metalMap", 3);

        glActiveTexture(GL_TEXTURE4);
        //.bind();
        setUniform("aoMap", 4);
    }
}
