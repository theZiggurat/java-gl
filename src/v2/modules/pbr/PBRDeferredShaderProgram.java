package v2.modules.pbr;

import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.system.RenderEngine;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.*;

public class  PBRDeferredShaderProgram extends ShaderProgram {

    public PBRDeferredShaderProgram(){
        super();

        createComputeShader("res/shaders/pbr_deferred_lighting_comp.cs");
        link();

        addUniform("camera_Pos");
        addUniform("light_Dir");

    }

    @Override
    public void compute(TextureObject albedo, TextureObject position, TextureObject normal,
                        TextureObject metal, TextureObject rough, TextureObject ao, TextureObject scene, Vector3f lightDir){

        bind();
        setUniform("camera_Pos", RenderEngine.instance().getMainCamera().getTranslation());
        setUniform("light_Dir", lightDir);
        bindImage(0, albedo.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(1, position.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, normal.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(3, metal.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(4, rough.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(5, rough.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(6, scene.getId(), GL_WRITE_ONLY, GL_RGBA16F);
        compute(16,16);
        unbind();
    }
}
