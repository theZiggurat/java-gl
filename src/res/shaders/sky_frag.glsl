# version 430

layout (location = 0) out vec4 pos_vbo;
layout (location = 1) out vec4 norm_vbo;
layout (location = 2) out vec4 albedo_vbo;
layout (location = 3) out float metal_vbo;
layout (location = 4) out float rough_vbo;
layout (location = 5) out float ao_vbo;

in vec3 pos_world;
uniform float scale;

void main(){

    float red =  0.15 * (pos_world.y+1600) / scale;
	float green =  0.23 * (pos_world.y+1600) / scale;
	float blue =  0.8 * (pos_world.y+1600) / scale;

    pos_vbo = vec4(0,0,0,1);
    norm_vbo = vec4(0,0,0,1);
    albedo_vbo = vec4(red,green,blue,1);
    metal_vbo = 0;
    rough_vbo = 0;
    ao_vbo = 0;
}