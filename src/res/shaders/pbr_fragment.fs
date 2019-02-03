

# version 430

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
layout (location = 3) out float metal_vbo;
layout (location = 4) out float rough_vbo;
layout (location = 5) out float ao_vbo;

in VS_DATA {
    vec2 uv;
    vec3 norm;
    vec3 pos_view;
    vec3 pos_world;
} vs;


uniform int map_albedo;
uniform sampler2D albedoMap;
uniform vec3 albedoConst;

uniform int map_normal;
uniform sampler2D normalMap;

uniform int map_roughness;
uniform sampler2D roughnessMap;
uniform float roughnessConst;

uniform int map_metal;
uniform sampler2D metalMap;
uniform float metalConst;

uniform mat4 viewMatrix;

uniform vec3 randomVec = vec3(1,0,0);
in mat3 tbn;


void main(){

    //vec3 norm = (viewMatrix * vec4(vs.norm,0)).xyz;
    vec3 norm = vs.norm;

    vec2 uv = vec2(vs.uv.x,1-vs.uv.y);


    vec3 norm_sample = texture(normalMap, uv).xyz;
    if(map_normal == 1){
        // gram-schmidt process to transform normals from tangent space to world space
        vec3 tangent   = normalize(randomVec - norm * dot(randomVec, norm));
        vec3 bitangent = cross(norm, tangent);
        mat3 TBN       = mat3(tangent, bitangent, norm);

        vec3 packed_norm = normalize(TBN * norm_sample) * 0.5 + 0.5;
        norm_vbo = vec4(packed_norm,1);

    } else {
        // just use model normal
        norm_vbo = vec4(normalize(norm),1.0);
    }

    if(map_albedo == 1)
        albedo_vbo = texture(albedoMap, uv);
    else
        albedo_vbo = vec4(albedoConst, 1);

    if(map_roughness == 1)
        rough_vbo = texture(roughnessMap, uv).r;
    else
        rough_vbo = roughnessConst;

    if(map_metal == 1)
        metal_vbo = texture(metalMap, uv).r;
    else
        metal_vbo = metalConst;

    pos_vbo = vec4(vs.pos_world, 1);

}