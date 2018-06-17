# version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 3) in vec3 normals;

out vec2 outTextureCoord;

uniform mat4 orthoMatrix;

void main(){
    gl_Position = orthoMatrix * vec4(position, 1.0);
    outTextureCoord = textureCoord;
}