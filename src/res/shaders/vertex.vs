#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 vertexNormal;

out vec2 outTextureCoord;
out vec3 lightVertexNormal;
out vec3 lightVertexPos;
out mat4 outModelViewMatrix;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    outTextureCoord = textureCoord;

    lightVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    lightVertexPos = mvPos.xyz;
    outModelViewMatrix = modelViewMatrix;
}