package v2.modules.generic;

import v2.engine.scene.node.ModuleNode;
import v2.engine.scene.Camera;
import v2.engine.system.Shader;
import v2.engine.utils.Color;

public class PlainColorShader extends Shader {

    private static Color color = new Color(0xe28c0b);

    private static PlainColorShader instance;
    public static PlainColorShader instance(){
        if (instance == null)
            instance = new PlainColorShader();
        return instance;
    }

    public static void setColor(Color _color){
         color = _color;
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
