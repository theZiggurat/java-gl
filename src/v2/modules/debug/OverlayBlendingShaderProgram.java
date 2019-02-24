package v2.modules.debug;

import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OverlayBlendingShaderProgram extends ShaderProgram {

    private static OverlayBlendingShaderProgram instance;
    public static OverlayBlendingShaderProgram instance(){
        if(instance == null)
            instance = new OverlayBlendingShaderProgram();
        return instance;
    }

    public OverlayBlendingShaderProgram(){
        super();
        createComputeShader("res/shaders/overlay/overlay_blend_cs.glsl");
        link();

    }

    @Override
    public void compute(TextureObject scene, TextureObject overlay, TextureObject dest) {

        bind();
        bindImage(0, scene.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(1, overlay.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(2, dest.getId(), GL_WRITE_ONLY, GL_RGBA16F);

        compute(16,16);
        unbind();
    }
}
