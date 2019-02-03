# version 430

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
layout (location = 3) out float metal_vbo;
layout (location = 4) out float rough_vbo;
layout (location = 5) out float ao_vbo;


void main(){

    albedo_vbo = vec4(1,0,0,1);

}