package v2.modules.sky;

import org.joml.Matrix4f;
import v2.engine.scene.node.ModuleNode;
import v2.engine.system.Shader;

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
        setUniform("viewProjectionMatrix",
                new Matrix4f(boundContext.getCamera().getViewProjectionMatrix()));
        setUniform("modelMatrix",
                new Matrix4f(skyNode.getModelMatrix()));
        setUniform("scale", skyNode.getWorldScaling().y);

    }
}
