# version 430

// very good atmospheric shaders provided by
// https://github.com/benanders/Hosek-Wilkie/blob/master/src/shaders/frag.glsl

layout (location = 3) out vec4 scene_vbo;

in vec3 fragPos;
//uniform float scale;

void main(){

//    float red =  0.15 * (pos_world.y+1600) / scale;
//	float green =  0.23 * (pos_world.y+1600) / scale;
//	float blue =  0.8 * (pos_world.y+1600) / scale;
    scene_vbo = vec4(fragPos, 1);
}