package v2.modules.pbr;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Module;
import v2.engine.utils.ImageLoader;

public class PBRMaterial extends Module {

    @Setter @Getter
    TextureObject albedoMap, normalMap, roughnessMap, metalMap;

    @Setter @Getter
    private float metalConst, roughnessConst;

    @Setter @Getter
    private Vector3f albedoConst;

    @Getter @Setter float IOR;


    private Boolean is_albedo_map, is_normal_map, is_roughness_map, is_metal_map;

    public PBRMaterial(float albedo_r, float albedo_g, float albedo_b,
                       float roughness, float metal)
    {
        this(new Vector3f(albedo_r, albedo_g, albedo_b), roughness, metal);
    }

    public PBRMaterial(String texturePath, boolean srgb){
        this(texturePath, "png", srgb);
    }

    public PBRMaterial(String texturePath, String fileExt, boolean srgb){

        this(texturePath,   "albedo." + fileExt ,
                            "normal." + fileExt,
                         "rough." + fileExt,
                             "metal." + fileExt, srgb);
    }

    public PBRMaterial(String texturePath, String albedoFile, String normalFile,
                       String roughnessFile, String metalFile, boolean srgb){

        TextureObject albedo = ImageLoader.loadTexture(
                texturePath + albedoFile, srgb)
                .bilinearFilter().wrap();
        setAlbedoMap(albedo);

        TextureObject normal = ImageLoader.loadTexture(
                texturePath + normalFile, srgb)
                .bilinearFilter().wrap();
        setNormalMap(normal);

        TextureObject roughness = ImageLoader.loadTexture(
                texturePath + roughnessFile, srgb)
                .bilinearFilter().wrap();
        setRoughnessMap(roughness);

        TextureObject metal = ImageLoader.loadTexture(
                texturePath + metalFile, srgb)
                .bilinearFilter().wrap();
        setMetalMap(metal);

        useAllMaps(true);

    }


    public PBRMaterial(Vector3f albedo, float roughness, float metal){
        this.albedoConst = albedo;
        this.roughnessConst = roughness;
        this.metalConst = metal;
        useAllMaps(false);
    }

    public PBRMaterial(){
        this(new Vector3f(1,1,1), 0f, 0f);
    }

    public void useAllMaps(Boolean use){
        this.is_metal_map = use;
        this.is_roughness_map = use;
        this.is_albedo_map = use;
        this.is_normal_map = use;
    }

    public Boolean isAlbedoMapped(){ return is_albedo_map && albedoMap != null; }
    public Boolean isNormalMapped(){ return is_normal_map && normalMap != null; }
    public Boolean isRoughnessMapped(){ return is_roughness_map && roughnessMap != null; }
    public Boolean isMetalMapped(){ return is_metal_map && metalMap != null; }

    public void useAlbedoMap(Boolean use){ this.is_albedo_map = use; }
    public void useNormalMap(Boolean use){ this.is_normal_map = use; }
    public void useRoughnessMap(Boolean use){ this.is_roughness_map = use; }
    public void useMetalMap(Boolean use){ this.is_metal_map = use; }

}
