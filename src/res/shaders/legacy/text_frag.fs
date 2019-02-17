#version 330

in vec2 outTextureCoord;
in vec2 textPos;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 color;

void main(){
    fragColor = color * texture(textureSampler, outTextureCoord);
}