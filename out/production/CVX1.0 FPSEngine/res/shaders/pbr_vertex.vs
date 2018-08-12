# version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 norm;

out vec2 uv_frag;
out vec3 norm_frag;
out vec3 pos_frag;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
    vec4 modelViewCoord = modelViewMatrix * vec4(pos, 1.0);
    gl_Position = projectionMatrix * modelViewCoord;
    uv_frag = vec2(uv.y, uv.x);
    norm_frag = normalize(modelViewMatrix * vec4(norm, 0.0)).xyz;
    pos_frag = modelViewCoord.xyz;
}