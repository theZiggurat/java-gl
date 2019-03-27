package v2.modules.post.ssao;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.system.Config;
import v2.engine.system.Core;
import v2.engine.system.Shader;
import v2.engine.system.Window;
import v2.modules.pbr.PBRPipeline;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class SSAOShader extends Shader {

    private List<Vector3f> samples;
    private Vector3f[] randvec;

    @Getter private TextureObject targetTexture;

    public SSAOShader(){

        targetTexture = new TextureObject(Window.instance().getWidth(), Window.instance().getHeight())
                            .allocateImage2D(GL_RGBA32F, GL_RGBA)
                            .nofilter();

        randvec = new Vector3f[16];
        float [] random = RandomKernel.generateXYNoise(16);
        for(int i = 0; i < 32;){
            randvec[i/2] = new Vector3f(random[i++], random[i++], 0);
        }

        samples = RandomKernel.generate3fHemisphere(Config.instance().getSsaoSamples());

        createComputeShader("res/shaders/ssao/ssao_comp.glsl");
        link();

        addUniform("resX");
        addUniform("resY");
        addUniform("radius");

        addUniform("numSamples");
//
        addUniform("viewMatrix");
        addUniform("projectionMatrix");
        for(int i = 0; i<Config.instance().getSsaoSamples(); i++){
            addUniform("samples["+i+"]");
        }

        for(int i = 0; i < 16; i++){
            addUniform("randvec["+i+"]");
        }
    }

    public void compute(TextureObject world_position, TextureObject normal){


        bind();
        setUniform("numSamples", Config.instance().getSsaoSamples());
        setUniform("radius", Config.instance().getSsaoRadius());

        setUniform("resX", Window.instance().getWidth());
        setUniform("resY", Window.instance().getHeight());

        setUniform("viewMatrix", Core.camera().getViewMatrix());
        setUniform("projectionMatrix", Core.camera().getProjectionMatrix());


        for(int i = 0; i<Config.instance().getSsaoSamples(); i++){
            setUniform("samples["+i+"]", samples.get(i));
        }

        for(int i = 0; i < 16; i++){
            setUniform("randvec["+i+"]", randvec[i]);
        }

        bindImage(0, world_position.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(1, normal.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, targetTexture.getId(), GL_WRITE_ONLY, GL_RGBA32F);

        compute(16,16);
        unbind();

    }
}
