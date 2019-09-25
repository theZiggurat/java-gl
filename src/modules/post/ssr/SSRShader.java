package modules.post.ssr;

import engine.glapi.TextureObject;
import engine.system.Config;
import engine.glapi.Shader;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_READ_WRITE;
import static org.lwjgl.opengl.GL30.GL_R16F;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class SSRShader extends Shader {

    SSRShader(){
        createComputeShader("res/shaders/ssr/ssr_cs.glsl");
        link();

        addUniform("resolution");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");

        addUniform("raymarchSteps");
        addUniform("binarySearchSteps");
        addUniform("rayStepLen");
        addUniform("falloffExp");
        addUniform("sampleCount");
    }


    public void compute(TextureObject worldPos, TextureObject worldNorm,
                        TextureObject ao, TextureObject out){

        bind();

        setUniform("resolution", boundContext.getResolution());
        setUniform("viewMatrix", boundContext.getCamera().getViewMatrix());
        setUniform("projectionMatrix", boundContext.getCamera().getProjectionMatrix());

        setUniform("raymarchSteps", Config.instance().getSsrRaymarchSteps());
        setUniform("binarySearchSteps", Config.instance().getSsrBinarySearchSteps());
        setUniform("rayStepLen", Config.instance().getSsrRayStepLen());
        setUniform("falloffExp", Config.instance().getSsrFalloff());
        setUniform("sampleCount", Config.instance().getSsrSamples());

        bindImage(0, worldPos.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(1, worldNorm.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, ao.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(3, out.getId(), GL_READ_WRITE, GL_RGBA16F);

        compute(16,16);
        unbind();

    }
}
