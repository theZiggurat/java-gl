package v2.modules.generic;

import v2.engine.scene.node.ModuleNode;
import v2.engine.scene.Camera;
import v2.engine.system.Shader;

public class DepthShader extends Shader {

    private static DepthShader instance;
    public static DepthShader instance(){
        if (instance == null)
            instance = new DepthShader();
        return instance;
    }

    private DepthShader(){
        super();
        createVertexShader("res/shaders/depth/depth_vs.glsl");
        createFragmentShader("res/shaders/depth/depth_fs.glsl");
        link();

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode node) {
        Camera camera = boundContext.getCamera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }
}
