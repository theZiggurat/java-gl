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
    vec3 pos;
    vec3 pos_world;
} vs;

uniform sampler2D albedoMap;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D metalMap;
uniform sampler2D aoMap;

uniform vec3 randomVec = vec3(1,0,0);


void main(){

    pos_vbo = vec4(vs.pos_world, 1);

    /* TBN METHOD */
    vec3 norm_sample = texture(normalMap, vs.uv).rgb;
    if(length(norm_sample) <1){
        norm_vbo = vec4(normalize(vs.norm),1.0);
    } else {
        vec3 tangent   = normalize(randomVec - vs.norm * dot(randomVec, vs.norm));
        vec3 bitangent = cross(vs.norm, tangent);
        mat3 TBN       = mat3(tangent, bitangent, vs.norm);
        norm_vbo = vec4(normalize(TBN * norm_sample),1);
    }

    /* DIFF METHOD */
    //vec3 diff = normalize(texture(normalMap, uv_vs).rgb*2-1) - vec3(0,0,1);
    //norm_vbo = vec4(normalize(vs.norm + diff),1.0);

    /* NO MAP */

    albedo_vbo = texture(albedoMap, vs.uv);
    //albedo_vbo = mix(vec4(uv_vs, 0, 1), albedo_vbo, .01);
    metal_vbo = texture(metalMap, vs.uv).r;
    rough_vbo = texture(roughnessMap, vs.uv).r;
    ao_vbo = texture(aoMap, vs.uv).r;

}