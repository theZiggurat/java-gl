package v2.modules.generic;

import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.Core;
import v2.engine.system.Shader;

public class DepthShader extends Shader {

    private static DepthShader instance;
    public static DepthShader instance(){
        if (instance == null)
            instance = new DepthShader();
        return instance;
    }

    private DepthShader(){
        createVertexShader("res/shaders/depth/depth_vs.glsl");
        createFragmentShader("res/shaders/depth/depth_fs.glsl");
        link();

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode node) {
        Camera camera = Core.camera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }
}
