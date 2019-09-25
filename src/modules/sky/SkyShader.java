package modules.sky;

import engine.scene.node.ModuleNode;
import engine.glapi.Shader;

public class SkyShader extends Shader {

    private static SkyShader instance;
    public static SkyShader instance(){
        if (instance == null){
            instance = new SkyShader();
        }
        return instance;
    }

    private SkyShader(){

        createVertexShader("res/shaders/sky/sky_vert.glsl");
        createFragmentShader("res/shaders/sky/sky_frag.glsl");
        link();

        addUniform("viewProjectionMatrix");
        addUniform("modelMatrix");

        addUniform("scale");
    }

    @Override
    public void updateUniforms(ModuleNode skyNode) {
        setUniform("viewProjectionMatrix", boundContext.getCamera().getViewProjectionMatrix());
        setUniform("modelMatrix", skyNode.getModelMatrix());
        setUniform("scale", skyNode.getWorldScaling().y);

    }
}
