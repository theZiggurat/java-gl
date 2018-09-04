# version 430

in vec2 uv_frag;
in vec3 norm_frag;
in vec3 pos_frag;

layout (location = 0) out vec4 albedo_vbo;
//layout (location = 1) out vec4 norm_vbo;
//layout (location = 2) out vec4 albedo_vbo;
//layout (location = 3) out float metal_vbo;
//layout (location = 4) out float rough_vbo;


void main(){
    //pos_vbo = vec4(pos_frag, 1.0);
    //norm_vbo = vec4(norm_frag,1.0);
    albedo_vbo = vec4(1,0,0,1);
    //metal_vbo = 0;
    //rough_vbo = .5f;
}