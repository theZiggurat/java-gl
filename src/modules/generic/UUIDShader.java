package modules.generic;

import engine.scene.Picking;
import engine.scene.node.ModuleNode;
import engine.scene.Camera;
import engine.glapi.Shader;

public class UUIDShader extends Shader {

    private static UUIDShader instance;
    public static UUIDShader instance(){
        if (instance == null)
            instance = new UUIDShader();
        return instance;
    }

    private UUIDShader(){
        super();
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
        Camera camera = boundContext.getCamera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("color", Picking.getUUIDColor(node.getUUID()));
    }
}
