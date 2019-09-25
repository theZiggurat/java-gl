package modules.shadow;

import engine.scene.light.LightManager;
import engine.scene.node.ModuleNode;
import engine.glapi.Shader;

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
