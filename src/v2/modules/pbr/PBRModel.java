package v2.modules.pbr;

import v2.engine.buffer.MeshVBO;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;

public class PBRModel extends ModuleNode {

    public PBRModel(MeshVBO mesh, PBRMaterial material){

        super();

        RenderModule renderer = new RenderModule(
                PBRShaderProgram.getInstance(), mesh);

        addModule(ModuleType.RENDER_MODULE, renderer);
        addModule(ModuleType.MATERIAL, material);


    }

    public void update(){
        super.update();
    }

    public void render(){
        PBRShaderProgram.getInstance().updateUniforms(this);
        super.render();
    }
}
