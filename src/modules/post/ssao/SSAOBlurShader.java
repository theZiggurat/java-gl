package modules.post.ssao;

import engine.glapi.TextureObject;
import engine.glapi.Shader;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_R16F;

public class SSAOBlurShader extends Shader {


    SSAOBlurShader(){

        createComputeShader("shaders/ssao/ssao_blur_cs.glsl");
        link();

        addUniform("resolution");
    }

    void compute(TextureObject preBlur, TextureObject target){

        bind();
        setUniform("resolution", boundContext.getResolution());

        bindImage(0, preBlur.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(1, target.getId(), GL_WRITE_ONLY, GL_R16F);

        compute(16,16, boundContext.getResolution());
        unbind();
    }
}
