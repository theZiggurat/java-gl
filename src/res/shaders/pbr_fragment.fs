# version 430

in vec2 uv_frag;
in vec3 norm_frag;
in vec3 pos_frag;

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
layout (location = 3) out float metal_vbo;
layout (location = 4) out float rough_vbo;

uniform sampler2D albedoMap;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D metalMap;
uniform sampler2D aoMap;


void main(){
    pos_vbo = vec4(pos_frag, 1);
    vec3 diff = texture(normalMap, uv_frag).xyz - vec3(0,0,1);
    norm_vbo = vec4(normalize(norm_frag + diff), 1);
    albedo_vbo = mix(texture(albedoMap, uv_frag), texture(aoMap, uv_frag), .5);
    metal_vbo = texture(metalMap, uv_frag).r;
    rough_vbo = texture(roughnessMap, uv_frag).r;
}