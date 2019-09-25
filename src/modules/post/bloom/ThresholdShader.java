package modules.post.bloom;

import engine.glapi.TextureObject;
import engine.glapi.Shader;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class ThresholdShader extends Shader {

    public ThresholdShader(){
        createComputeShader("res/shaders/bloom/threshold_cs.glsl");
        link();

        addUniform("threshold");

    }

    public void compute(TextureObject src, TextureObject dest){

        bindImage(0, src.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(1, dest.getId(), GL_WRITE_ONLY, GL_RGBA16F);

        setUniform("threshold", 1f);

    }


}
