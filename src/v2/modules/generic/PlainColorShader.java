package v2.modules.generic;

import org.joml.Vector3f;
import v2.engine.scene.node.ModuleNode;
import v2.engine.scene.Camera;
import v2.engine.system.Shader;

public class PlainColorShader extends Shader {

    private static Vector3f color = new Vector3f(1,1,1);

    private static PlainColorShader instance;
    public static PlainColorShader instance(){
        if (instance == null)
            instance = new PlainColorShader();
        return instance;
    }

    public static void setColor(Vector3f _color){
         color = _color;
    }

    public static void setColor(float r, float g, float b){
        setColor(new Vector3f(r, g, b));
    }

    private PlainColorShader(){
        super();
        createVertexShader("res/shaders/overlay/overlay_vs.glsl");
        createFragmentShader("shaders/gui/panel_fs.glsl");
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
        setUniform("color", color);
    }
}
