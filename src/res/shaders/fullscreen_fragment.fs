#version 330

in vec2 uv;

layout (location = 0) out vec4 fragColor;

uniform sampler2D texture;

void main(){
    fragColor = texture2D(texture, uv);
}