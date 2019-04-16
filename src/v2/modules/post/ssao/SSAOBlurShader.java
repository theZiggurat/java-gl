package v2.modules.post.ssao;

import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Shader;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_R16F;

public class SSAOBlurShader extends Shader {


    public SSAOBlurShader(){

        createComputeShader("res/shaders/ssao/ssao_blur_comp.glsl");
        link();

        addUniform("resolution");
    }

    public void compute(TextureObject preBlur, TextureObject target){

        bind();
        setUniform("resolution", boundContext.getResolution());

        bindImage(0, preBlur.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(1, target.getId(), GL_WRITE_ONLY, GL_R16F);

        compute(16,16, boundContext.getResolution());
        unbind();
    }
}
