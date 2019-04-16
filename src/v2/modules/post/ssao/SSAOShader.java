package v2.modules.post.ssao;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Config;
import v2.engine.system.Shader;
import v2.engine.system.Window;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.*;

public class SSAOShader extends Shader {

    private List<Vector3f> samples;
    private Vector3f[] randvec;

    public SSAOShader(){

        randvec = new Vector3f[16];
        float [] random = RandomKernel.generateXYNoise(16);
        for(int i = 0; i < 32;){
            randvec[i/2] = new Vector3f(random[i++], random[i++], 0);
        }

        samples = RandomKernel.generate3fHemisphere(
                Config.instance().getSsaoSamples()
        );

        createComputeShader("res/shaders/ssao/ssao_comp.glsl");
        link();

        addUniform("resolution");
        addUniform("radius");
        addUniform("numSamples");

        addUniform("viewMatrix");
        addUniform("projectionMatrix");
        for(int i = 0; i<Config.instance().getSsaoSamples(); i++){
            addUniform("samples["+i+"]");
        }

        for(int i = 0; i < 16; i++){
            addUniform("randvec["+i+"]");
        }
    }

    public void compute(TextureObject worldPos, TextureObject normal, TextureObject target){

        bind();

        setUniform("numSamples", Config.instance().getSsaoSamples());
        setUniform("radius", Config.instance().getSsaoRadius());

        setUniform("resolution", boundContext.getResolution());
        setUniform("viewMatrix", boundContext.getCamera().getViewMatrix());
        setUniform("projectionMatrix", boundContext.getCamera().getProjectionMatrix());

        for(int i = 0; i<Config.instance().getSsaoSamples(); i++)
            setUniform("samples["+i+"]", samples.get(i));

        for(int i = 0; i < 16; i++)
            setUniform("randvec["+i+"]", randvec[i]);

        bindImage(0, worldPos.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(1, normal.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, target.getId(), GL_WRITE_ONLY, GL_R16F);

        compute(16,16, boundContext.getResolution());
        unbind();

    }
}
