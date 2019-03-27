#version 430

layout (location = 0) in vec3 pos;

uniform vec2 scale, position;

void main() {
    vec2 coord = pos.xy;
    coord *= scale;
    coord += position;
    gl_Position = vec4(coord, 1, 1);
}
