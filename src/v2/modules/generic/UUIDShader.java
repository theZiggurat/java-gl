package v2.modules.generic;

import v2.engine.event.Picking;
import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.Config;
import v2.engine.system.Core;
import v2.engine.system.Shader;

public class UUIDShader extends Shader {

    private static UUIDShader instance;
    public static UUIDShader instance(){
        if (instance == null)
            instance = new UUIDShader();
        return instance;
    }

    private UUIDShader(){
        createVertexShader("res/shaders/picking/UUID_vs.glsl");
        createFragmentShader("res/shaders/picking/UUID_fs.glsl");
        link();

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");

        addUniform("color");
    }

    @Override
    public void updateUniforms(ModuleNode node) {
        Camera camera = Core.camera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("color", Picking.getUUIDColor(node.getUUID()));
    }
}
