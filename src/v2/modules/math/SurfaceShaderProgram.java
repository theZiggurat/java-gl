package v2.modules.math;

import v2.engine.scene.ModuleNode;
import v2.engine.system.ShaderProgram;

public class SurfaceShaderProgram extends ShaderProgram {

    private static SurfaceShaderProgram instance;
    public static SurfaceShaderProgram instance(){
        if(instance() == null)
            instance = new SurfaceShaderProgram();
        return instance;
    }

    private SurfaceShaderProgram(){

        createVertexShader("res/shaders/surface_vertex.glsl");
        createFragmentShader("res/shaders/surface_frag.glsl");


    }

    @Override
    public void updateUniforms(ModuleNode surface){


    }
}
