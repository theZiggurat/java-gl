# version 430

layout (location = 2) out vec4 albedo_vbo;

in vec3 pos_world;
uniform float scale;

void main(){

    float red =  0.15 * (pos_world.y+1600) / scale;
	float green =  0.23 * (pos_world.y+1600) / scale;
	float blue =  0.8 * (pos_world.y+1600) / scale;
    albedo_vbo = vec4(red,1,1,1);
}