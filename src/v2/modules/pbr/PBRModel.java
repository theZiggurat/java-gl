package v2.modules.pbr;

import lombok.Getter;
import v2.engine.gldata.vbo.Mesh3D;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.RenderModule;
import v2.engine.scene.RenderType;
import v2.modules.generic.DepthShader;
import v2.modules.generic.UUIDShader;
import v2.modules.shadow.ShadowShader;
import v2.modules.generic.PlainColorShader;

import java.util.ArrayList;

public class PBRModel extends ModuleNode {

    @Getter Mesh3D mesh;
    @Getter PBRMaterial material;

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

        RenderModule wireframerenderer = new RenderModule(
                PlainColorShader.instance(), mesh
        );

        RenderModule UUIDrenderer = new RenderModule(
                UUIDShader.instance(), mesh
        );

        RenderModule depthrenderer = new RenderModule(
                DepthShader.instance(), mesh
        );

        addModule(RenderType.TYPE_SCENE, scenerenderer);
        addModule(RenderType.TYPE_SHADOW, shadowrenderer);
        addModule(RenderType.TYPE_WIREFRAME, wireframerenderer);
        addModule(RenderType.TYPE_UUID, UUIDrenderer);
        addModule(RenderType.TYPE_DEPTH, depthrenderer);

    }

    public PBRModel(ArrayList<Mesh3D> meshs, PBRMaterial material){
        super();
        for(Mesh3D mesh: meshs){
            PBRModel model = new PBRModel(mesh, material);
            addChild(model);
        }
    }
}
