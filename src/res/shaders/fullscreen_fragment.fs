#version 330

in vec2 uv;

layout (location = 0) out vec4 fragColor;

uniform sampler2D texture;

void main(){
    vec2 tex = vec2(uv.x, 1 - uv.y);
    fragColor = texture2D(texture, tex);
}