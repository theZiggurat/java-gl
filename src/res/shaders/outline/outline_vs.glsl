# version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 norm;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform float offset;

void main() {
    vec3 worldPos = (modelMatrix * vec4(pos, 1)).xyz;
    vec3 worldNorm = normalize((modelMatrix * vec4(norm, 0)).xyz);

    vec3 pushedWorldPos = worldPos + worldNorm * offset;

    gl_Position = projectionMatrix * viewMatrix * vec4(pushedWorldPos, 1);
}