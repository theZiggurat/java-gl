package v2.modules.post.ssao;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.rand.NoiseShaderProgram;
import v2.engine.rand.RandomKernel;
import v2.engine.system.RenderEngine;
import v2.engine.system.ShaderProgram;
import v2.engine.system.Window;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

public class SSAOShaderProgram extends ShaderProgram {

    private TextureObject noise;
    private List<Vector3f> samples;

    @Getter private TextureObject targetTexture;

    private final int numSamples = 64;

    public SSAOShaderProgram(){

        targetTexture = new TextureObject(Window.instance().getWidth(), Window.instance().getWidth())
                            .allocateImage2D(GL_RGBA16F, GL_RGBA)
                            .bilinearFilter()
                            .wrap();

        noise = NoiseShaderProgram.instance().make().getTargetTexture();
        samples = RandomKernel.generate3fHemisphere(numSamples);

        createComputeShader("res/shaders/ssao_comp.glsl");
        link();

        addUniform("resX");
        addUniform("resY");

        addUniform("noise");
        addUniform("numSamples");

        addUniform("viewMatrix");
        addUniform("projectionMatrix");

        addUniform("z_far");
        for(int i = 0; i<numSamples; i++){
            addUniform("samples["+i+"]");
        }
    }

    public void compute(TextureObject world_position, TextureObject normal){

        activeTexture(0);
        noise.bind();
        setUniform("noise", 0);

        setUniform("numSamples", numSamples);

        setUniform("resX", Window.instance().getWidth());
        setUniform("resY", Window.instance().getHeight());

        setUniform("viewMatrix", RenderEngine.instance().getMainCamera().getViewMatrix());
        setUniform("projectionMatrix", RenderEngine.instance().getMainCamera().getProjectionMatrix());

        setUniform("z_far", RenderEngine.instance().getMainCamera().getZFAR());

        for(int i = 0; i<numSamples; i++){
            setUniform("samples["+i+"]", samples.get(i));
        }

        bindImage(0, targetTexture.getId(), GL_WRITE_ONLY, GL_RGBA16F);
        bindImage(1, world_position.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, normal.getId(), GL_READ_ONLY, GL_RGBA32F);

        compute(16,16);

    }
}
