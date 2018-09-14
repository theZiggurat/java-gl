# version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

out vec2 outTextureCoord;

uniform mat4 orthoMatrix;

void main(){
    gl_Position = orthoMatrix * vec4(position, 0.0, 1.0);
    outTextureCoord = textureCoord;
}