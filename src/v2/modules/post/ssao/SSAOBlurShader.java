package v2.modules.post.ssao;

import lombok.Getter;
import v2.engine.gldata.TextureObject;
import v2.engine.system.Shader;
import v2.engine.system.Window;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class SSAOBlurShader extends Shader {

    @Getter private TextureObject targetTexture;

    public SSAOBlurShader(){

        targetTexture = new TextureObject(
                Window.instance().getWidth(),
                Window.instance().getHeight())
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .nofilter();

        createComputeShader("res/shaders/ssao/ssao_blur_comp.glsl");
        link();

        addUniform("resX");
        addUniform("resY");
    }

    public void compute(TextureObject ssao){

        bind();
        setUniform("resX", Window.instance().getWidth());
        setUniform("resY", Window.instance().getHeight());

        bindImage(0, ssao.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(1, targetTexture.getId(), GL_WRITE_ONLY, GL_RGBA32F);

        compute(16,16);
        unbind();
    }
}
