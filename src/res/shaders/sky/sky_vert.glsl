# version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 norm;

out vec3 fragPos;

uniform mat4 modelMatrix;
uniform mat4 viewProjectionMatrix;

void main(){

    fragPos = normalize(pos);
    gl_Position = viewProjectionMatrix * modelMatrix * vec4(pos, 1);

}