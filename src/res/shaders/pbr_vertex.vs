# version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 uv;
layout (location = 2) in vec3 norm;

out vec2 uv_vs;
out vec3 norm_vs;
out vec3 pos_vs;
out vec3 pos_world;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(){
    vec4 modelCoord = modelMatrix * vec4(pos, 1.0);
    gl_Position = projectionMatrix * viewMatrix * modelCoord;
    pos_world = modelCoord.xyz;
    uv_vs = vec2(uv.x, uv.y);
    norm_vs = normalize(modelMatrix * vec4(norm, 0)).xyz;
    //norm_vs = normalize(norm);
    //norm_vs = (viewMatrix * modelMatrix * vec4(norm,0)).xyz;
    pos_vs = gl_Position.xyz;
}