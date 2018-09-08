# version 430

in vec2 uv_frag;
in vec3 norm_frag;
in vec3 pos_frag;

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
//layout (location = 3) out float metal_vbo;
//layout (location = 4) out float rough_vbo;

uniform sampler2D albedoMap;


void main(){
    vec2 uv = vec2(1-uv_frag.x, uv_frag.y);
    pos_vbo = vec4(pos_frag, 1);
    norm_vbo = vec4(norm_frag, 1);
    albedo_vbo = mix(texture(albedoMap, uv), vec4(.8, .5, .1, 1), .9);
    //metal_vbo = 0;
    //rough_vbo = .3f;
}