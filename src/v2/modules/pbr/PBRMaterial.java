package v2.modules.pbr;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.scene.Module;

public class PBRMaterial extends Module {

    @Setter @Getter
    TextureObject albedoMap, normalMap, roughnessMap, metalMap;

    @Setter @Getter
    private float metalConst, roughnessConst;

    @Setter @Getter
    private Vector3f albedoConst;


    private Boolean is_albedo_map, is_normal_map, is_roughness_map, is_metal_map;

    public PBRMaterial(float albedo_r, float albedo_g, float albedo_b,
                       float roughness, float metal)
    {
        this(new Vector3f(albedo_r, albedo_g, albedo_b), roughness, metal);
    }


    public PBRMaterial(Vector3f albedo, float roughness, float metal){
        this.albedoConst = albedo;
        this.roughnessConst = roughness;
        this.metalConst = metal;

        this.is_albedo_map = false;
        this.is_normal_map = false;
        this.is_roughness_map = false;
        this.is_metal_map = false;
    }

    public PBRMaterial(){
        this(new Vector3f(1,1,1), 0f, .5f);
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
