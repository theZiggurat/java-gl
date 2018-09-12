# version 430

in vec2 uv_vs;
in vec3 norm_vs;
in vec3 pos_vs;
in vec3 pos_world;

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
layout (location = 3) out float metal_vbo;
layout (location = 4) out float rough_vbo;

uniform sampler2D albedoMap;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D metalMap;

//uniform vec3 randomVec;


void main(){

    pos_vbo = vec4(pos_world, 1);
    //vec3 tangent   = normalize(randomVec - norm_vs * dot(randomVec, norm_vs));
    //vec3 bitangent = cross(norm_vs, tangent);
    //mat3 TBN       = mat3(tangent, bitangent, norm_vs);
    //vec3 diff = normalize(texture(normalMap, uv_vs).rgb*2-1) - vec3(0,0,1);
    //norm_vbo = vec4(normalize(norm_vs + diff),1.0);
    vec3 norm_sample = texture(normalMap, uv_vs).rgb;
    //norm_vbo = vec4(normalize(TBN * norm_sample),1);
    norm_vbo = vec4(mix(norm_sample, norm_vs, .99),1);
    albedo_vbo = texture(albedoMap, uv_vs);
    //albedo_vbo = mix(vec4(uv_vs, 0, 1), albedo_vbo, .01);
    metal_vbo = texture(metalMap, uv_vs).r;
    rough_vbo = texture(roughnessMap, uv_vs).r;

}