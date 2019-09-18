package v2.modules.generic;

import v2.engine.glapi.tex.TextureObject;
import v2.engine.scene.SceneContext;
import v2.engine.system.Shader;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_READ_WRITE;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public class OverlayBlendingShader extends Shader {

    SceneContext context;

    public OverlayBlendingShader(SceneContext context){
        super();
        this.context = context;
        createComputeShader("res/shaders/overlay/overlay_blend_cs.glsl");
        link();

        //addUniform("resolution");

    }
    public void compute(TextureObject scene, TextureObject overlay) {

        bind();

        bindImage(0, scene.getId(), GL_READ_WRITE, GL_RGBA16F);
        bindImage(1, overlay.getId(), GL_READ_ONLY, GL_RGBA16F);

        //setUniform("resolution", context.getResolution());

        compute(16,16, boundContext.getResolution());
        unbind();
    }
}
