package modules.pbr;

import lombok.Getter;
import lombok.Setter;
import engine.glapi.vbo.Mesh3D;
import engine.scene.node.ModuleNode;
import engine.scene.node.RenderModule;
import engine.scene.node.RenderType;
import modules.generic.UUIDShader;
import modules.shadow.ShadowShader;
import modules.outline.OutlineShader;

import java.util.ArrayList;

public class PBRModel extends ModuleNode {

    @Getter Mesh3D mesh;
    @Getter PBRMaterial material;

    @Getter @Setter private float UVscalar = 1f;

    public PBRModel(Mesh3D mesh, PBRMaterial material){

        super();

        this.mesh = mesh;
        this.material = material;

        RenderModule scenerenderer = new RenderModule(
                PBRShader.instance(), mesh
        );

        RenderModule shadowrenderer = new RenderModule(
                ShadowShader.instance(), mesh
        );

        RenderModule outlinerenderer = new RenderModule(
                OutlineShader.instance(), mesh
        );

        RenderModule UUIDrenderer = new RenderModule(
                UUIDShader.instance(), mesh
        );

        addModule(RenderType.TYPE_SCENE, scenerenderer);
        addModule(RenderType.TYPE_SHADOW, shadowrenderer);
        addModule(RenderType.TYPE_OUTLINE, outlinerenderer);
        addModule(RenderType.TYPE_UUID, UUIDrenderer);

    }

    public PBRModel(ArrayList<Mesh3D> meshs, PBRMaterial material){
        super();
        for(Mesh3D mesh: meshs){
            PBRModel model = new PBRModel(mesh, material);
            addChild(model);
        }
    }
}
