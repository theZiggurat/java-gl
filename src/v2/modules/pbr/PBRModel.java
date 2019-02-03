package v2.modules.pbr;

import v2.engine.gldata.TextureObject;
import v2.engine.vbo.Mesh3D;
import v2.engine.vbo.VertexBufferObject;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.Node;
import v2.engine.scene.RenderModule;
import v2.engine.system.StaticLoader;

import java.util.ArrayList;

public class PBRModel extends ModuleNode {

    /**
     *
     * @param meshFile .obj file "res/models/___.obj"
     * @param texturePath file that holds all the textures "res/images/__/"
     * @param albedoFile albedo image texture with format "albedo.tga" for example
     * @param normalFile normal image texture with format "normal.png" for example
     * @param roughnessFile roughness image texture with format "rough.png" for example
     * @param metalFile metal image texture with format "metal.png" for example
     * @return
     */
    public static Node quickModel(String meshFile, String texturePath,
                                  String albedoFile, String normalFile, String roughnessFile, String metalFile,
                                  String aoFile, boolean srgb, boolean flipUV){

        PBRMaterial material = new PBRMaterial();
        if(albedoFile!=null) {
            TextureObject albedo = StaticLoader.loadTexture(
                    texturePath + albedoFile, srgb)
                    .bilinearFilter().wrap();
            material.setAlbedoMap(albedo);
            material.useAlbedoMap(true);
        }


        if(normalFile != null) {
            TextureObject normal = StaticLoader.loadTexture(
                    texturePath + normalFile, srgb)
                    .bilinearFilter().wrap();
            material.setNormalMap(normal);
            material.useNormalMap(true);
        }

        if(roughnessFile != null) {
            TextureObject roughness = StaticLoader.loadTexture(
                    texturePath + roughnessFile, srgb)
                    .bilinearFilter().wrap();
            material.setRoughnessMap(roughness);
            material.useRoughnessMap(true);
        }

        if(metalFile != null){
            TextureObject metal = StaticLoader.loadTexture(
                    texturePath + metalFile, srgb)
                    .bilinearFilter().wrap();
            material.setMetalMap(metal);
            material.useMetalMap(true);
        }

        Node ret = new Node();
        ArrayList<Mesh3D> meshs = StaticLoader.loadMeshGroup(meshFile);

        for(Mesh3D mesh: meshs){
            if(flipUV)
                mesh.getUVs().stream().forEach(e -> {
                e.x = 1-e.x;});

            PBRModel model = new PBRModel(mesh, material);
            ret.addChild(model);
        }

        return ret;
    }

    public PBRModel(VertexBufferObject mesh, PBRMaterial material){

        super();

        RenderModule renderer = new RenderModule(
                PBRShaderProgram.instance(), mesh);

        addModule(ModuleType.RENDER_MODULE, renderer);
        addModule(ModuleType.MATERIAL, material);


    }
}
