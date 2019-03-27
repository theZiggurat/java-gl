package v2.modules.generic;

import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.Config;
import v2.engine.system.Core;
import v2.engine.system.Shader;

import java.sql.Time;

public class WireframeShader extends Shader {


    private static WireframeShader instance;
    public static WireframeShader instance(){
        if (instance == null)
            instance = new WireframeShader();
        return instance;
    }

    private WireframeShader(){
        createVertexShader("res/shaders/wireframe/wire_vs.glsl");
        createFragmentShader("res/shaders/wireframe/wire_fs.glsl");
        link();

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");

        addUniform("time");
    }

    @Override
    public void updateUniforms(ModuleNode node) {
        Camera camera = Core.camera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());

        setUniform("time", ((float)((float)System.currentTimeMillis()/1000)));
    }
}
