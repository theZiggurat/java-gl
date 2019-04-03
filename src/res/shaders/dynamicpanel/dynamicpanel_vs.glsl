#version 330

in vec3 position_vert;
in vec2 uv_vert;

out vec2 uv;

void main(){
    gl_Position = vec4(position_vert, 1.0);
    uv = uv_vert;
}