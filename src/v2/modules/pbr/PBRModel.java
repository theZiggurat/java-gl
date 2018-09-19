package v2.modules.pbr;

import v2.engine.gldata.TextureObject;
import v2.engine.gldata.VertexBufferObject;
import v2.engine.javadata.MeshData;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;
import v2.engine.system.StaticLoader;

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
    public static PBRModel quickModel(String meshFile, String texturePath,
              String albedoFile, String normalFile, String roughnessFile, String metalFile, String aoFile, boolean srgb){

        TextureObject albedo = StaticLoader.loadTexture(
                texturePath + albedoFile, srgb)
                .bilinearFilter().wrap();

        TextureObject normal;
        if(normalFile != null) {
            normal = StaticLoader.loadTexture(
                    texturePath + normalFile, srgb)
                    .bilinearFilter().wrap();
        } else {
            normal = TextureObject.emptyTexture();
        }

        TextureObject roughness = StaticLoader.loadTexture(
                texturePath + roughnessFile, srgb)
                .bilinearFilter().wrap();


        TextureObject metal = StaticLoader.loadTexture(
                texturePath + metalFile, srgb)
                .bilinearFilter().wrap();
        TextureObject ao;
        if(aoFile != null) {
            ao = StaticLoader.loadTexture(
                    texturePath + aoFile, srgb)
                    .bilinearFilter().wrap();
        } else {
            ao = TextureObject.emptyTexture();
        }

        VertexBufferObject mesh = new VertexBufferObject(MeshData.loadMesh(meshFile));

        PBRMaterial material = new PBRMaterial(albedo, normal, roughness, metal, ao);

        return new PBRModel(mesh, material);
    }

    public PBRModel(VertexBufferObject mesh, PBRMaterial material){

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
