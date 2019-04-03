package v2.modules.generic;

import v2.engine.gldata.tex.TextureObject;
import v2.engine.system.Shader;
import v2.engine.system.Window;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OverlayBlendingShader extends Shader {

    private static OverlayBlendingShader instance;
    public static OverlayBlendingShader instance(){
        if(instance == null)
            instance = new OverlayBlendingShader();
        return instance;
    }

    public OverlayBlendingShader(){
        super();
        createComputeShader("res/shaders/overlay/overlay_blend_cs.glsl");
        link();

        addUniform("depth_s");
        addUniform("depth_o");

        addUniform("resX");
        addUniform("resY");

    }
    public void compute(TextureObject scene, TextureObject overlay,
                        TextureObject dest, TextureObject depth_scene, TextureObject depth_overlay) {

        bind();

        bindImage(0, scene.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(1, overlay.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(2, dest.getId(), GL_WRITE_ONLY, GL_RGBA16F);

        activeTexture(depth_scene, 0);
        setUniform("depth_s", 0);

        activeTexture(depth_overlay, 1);
        setUniform("depth_o", 1);

        setUniform("resX", Window.instance().getWidth());
        setUniform("resY", Window.instance().getHeight());

        compute(16,16);
        unbind();
    }
}
