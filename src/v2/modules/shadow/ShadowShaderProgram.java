package v2.modules.shadow;

import v2.engine.light.LightManager;
import v2.engine.scene.ModuleNode;
import v2.engine.system.ShaderProgram;

public class ShadowShaderProgram extends ShaderProgram {

    private static ShadowShaderProgram instance;
    public static ShadowShaderProgram instance(){
        if(instance == null)
            instance = new ShadowShaderProgram();
        return instance;
    }

    private ShadowShaderProgram(){
        createVertexShader("res/shaders/shadow/shadow_vs.glsl");
        createFragmentShader("res/shaders/shadow/shadow_fs.glsl");
        link();

        addUniform("lightSpaceMatrix");
        addUniform("modelMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode parent) {
        setUniform("lightSpaceMatrix", LightManager.getSun().getLightSpaceMatrix());
        setUniform("modelMatrix", parent.getModelMatrix());
    }
}
