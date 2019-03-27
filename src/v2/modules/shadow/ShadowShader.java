package v2.modules.shadow;

import v2.engine.scene.light.LightManager;
import v2.engine.scene.ModuleNode;
import v2.engine.system.Shader;

public class ShadowShader extends Shader {

    private static ShadowShader instance;
    public static ShadowShader instance(){
        if(instance == null)
            instance = new ShadowShader();
        return instance;
    }

    private ShadowShader(){
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
