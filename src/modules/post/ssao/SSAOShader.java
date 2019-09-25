package modules.post.ssao;

import org.joml.Vector3f;
import engine.glapi.TextureObject;
import engine.system.Config;
import engine.glapi.Shader;

import java.util.List;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.*;

public class SSAOShader extends Shader {

    private List<Vector3f> samples;
    private Vector3f[] randvec;

    SSAOShader(){

        randvec = new Vector3f[16];
        float [] random = RandomKernel.generateXYNoise(16);
        for(int i = 0; i < 32;){
            randvec[i/2] = new Vector3f(random[i++], random[i++], 0);
        }

        samples = RandomKernel.generate3fHemisphere(
                Config.instance().getSsaoSamples()
        );

        createComputeShader("shaders/ssao/ssao_cs.glsl");
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


    void compute(TextureObject worldPos, TextureObject normal, TextureObject target){

        bind();

        if(samples.size() != Config.instance().getSsaoSamples())
            samples = RandomKernel.generate3fHemisphere(Config.instance().getSsaoSamples());

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
