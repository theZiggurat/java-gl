package modules.outline;

import engine.scene.node.ModuleNode;
import engine.scene.Camera;
import engine.glapi.Shader;
import engine.utils.Color;

public class OutlineShader extends Shader {

    private static Color color = new Color(0xe28c0b);
    private static float offset = 0.0f;

    private static OutlineShader instance;
    public static OutlineShader instance(){
        if (instance == null)
            instance = new OutlineShader();
        return instance;
    }

    public static void setColor(Color _color){
         color = _color;
    }
    public static void setOffset(float _offset) { offset = _offset; }

    private OutlineShader(){
        super();
        createVertexShader("res/shaders/outline/outline_vs.glsl");
        createFragmentShader("res/shaders/outline/outline_fs.glsl");
        link();

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");

        addUniform("color");
        addUniform("offset");
    }

    @Override
    public void updateUniforms(ModuleNode node) {
        Camera camera = boundContext.getCamera();

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("color", color);
        setUniform("offset", offset);
        //setUniform("scaling", node.transform.getScaling());
    }
}
