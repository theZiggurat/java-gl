package v2.instances;

import v2.engine.buffer.GLTexture;
import v2.engine.buffer.MeshVBO;
import v2.engine.mesh.MeshData;
import v2.engine.scene.Node;
import v2.engine.scene.Scenegraph;
import v2.engine.system.Camera;
import v2.engine.system.EngineInterface;
import v2.engine.system.RenderEngine;
import v2.engine.system.StaticLoader;
import v2.modules.pbr.PBRMaterial;
import v2.modules.pbr.PBRModel;

public class PBRTest implements EngineInterface {

    private Scenegraph scene;

    @Override
    public void init(){

        MeshVBO mesh;

        GLTexture albedo = StaticLoader.loadTexture(
                "res/images/woodframe/albedo.png");

        mesh = new MeshVBO(MeshData.loadMesh("res/models/bus.obj"));

        PBRMaterial material = new PBRMaterial(albedo);

        PBRModel model = new PBRModel(mesh, material);

        scene = RenderEngine.getInstance().getScenegraph();
        Node object = new Node();
        object.addChild(model);
        scene.addChild(object);

    }

    @Override
    public void update(double duration) {
    }

    @Override
    public void cleanup() {

    }
}
