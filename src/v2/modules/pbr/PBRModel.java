package v2.modules.pbr;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.utils.AssimpLoader;
import v2.engine.utils.ImageLoader;
import v2.engine.gldata.vbo.Mesh3D;
import v2.engine.gldata.vbo.VertexBufferObject;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.Node;
import v2.engine.scene.RenderModule;
import v2.modules.shadow.ShadowShaderProgram;

import java.util.ArrayList;

public class PBRModel extends ModuleNode {

    @Getter Mesh3D mesh;

    public PBRModel(Mesh3D mesh, PBRMaterial material){

        super();

        this.mesh = mesh;

        RenderModule scenerenderer = new RenderModule(
                PBRShaderProgram.instance(), mesh);

        RenderModule shadowrenderer = new RenderModule(
                ShadowShaderProgram.instance(), mesh
        );

        addModule(ModuleType.RENDER_MODULE_SCENE, scenerenderer);
        addModule(ModuleType.RENDER_MODULE_SHADOW, shadowrenderer);
        addModule(ModuleType.MATERIAL, material);


    }

    public PBRModel(ArrayList<Mesh3D> meshs, PBRMaterial material){
        super();
        for(Mesh3D mesh: meshs){
            PBRModel model = new PBRModel(mesh, material);
            addChild(model);
        }
    }
}
