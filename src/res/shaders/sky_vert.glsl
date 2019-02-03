# version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 norm;

out vec3 pos_world;

uniform mat4 modelMatrix;
uniform mat4 viewProjectionMatrix;

void main(){

    vec4 pos_x = modelMatrix * vec4(pos, 1);
    pos_world = pos_x.xyz;
    gl_Position = viewProjectionMatrix * pos_x;

}