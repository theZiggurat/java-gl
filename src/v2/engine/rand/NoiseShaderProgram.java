package v2.engine.rand;

import lombok.Getter;
import org.joml.Vector2f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.ModuleNode;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGB32F;

public class NoiseShaderProgram extends ShaderProgram {

    private float[] noise_vec2;
    @Getter private TextureObject targetTexture;

    private static NoiseShaderProgram instance;

    public static NoiseShaderProgram instance(){
        if(instance == null){
            instance = new NoiseShaderProgram();
        }
        return instance;
    }

    private NoiseShaderProgram(){

        super();

        recalculateNoise();
        targetTexture = new TextureObject(GL_TEXTURE_2D,4,4)
                            .allocateImage2D(GL_RGB32F, GL_RGB)
                            .nofilter()
                            .wrap();

        createComputeShader("res/shaders/noise_comp.glsl");
        link();

        for(int i = 0; i<16; i++){
            addUniform("noise_vec2[" + i + "]");
        }

    }

    public void recalculateNoise(){
        noise_vec2 = RandomKernel.generateXYNoise(16);
    }

    @Override
    public void compute() {

        bind();
        for(int i = 0; i<32; i+=0){
            setUniform("noise_vec2["+i/2+"]", new Vector2f(noise_vec2[i++], noise_vec2[i++]));
        }
        bindImage(0, targetTexture.getId(), GL_WRITE_ONLY, GL_RGB32F);

        compute(1,1,4,4);
    }

    public NoiseShaderProgram make(){
        compute();
        return this;
    }
}
