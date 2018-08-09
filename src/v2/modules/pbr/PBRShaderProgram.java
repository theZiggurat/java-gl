package v2.modules.pbr;

import v2.engine.render.ShaderProgram;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;

public class PBRShaderProgram extends ShaderProgram {

    private static PBRShaderProgram instance = null;

    public static PBRShaderProgram getInstance(){
        if(instance == null){
            instance = new PBRShaderProgram();
        }
        return instance;
    }

    private PBRShaderProgram(){
        super();

    }

    @Override
    public void updateUniforms(ModuleNode group){

        PBRMaterial material = (PBRMaterial) group.getModules().get(ModuleType.MATERIAL);

    }

}
