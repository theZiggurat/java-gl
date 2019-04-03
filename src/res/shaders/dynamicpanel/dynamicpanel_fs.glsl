#version 330

in vec2 uv;

layout (location = 0) out vec4 fragColor;

uniform sampler2D texture;
uniform int isDepth;

const float zNear = 0.01f;
const float zFar = 100f;

float linearize_depth(float d)
{
    return (2.0 * zNear) / (zFar + zNear - d * (zFar - zNear));
}

void main(){
    vec2 tex = vec2(uv.x, 1 - uv.y);
    if(isDepth == 0)
        fragColor = texture2D(texture, tex);
    else
        fragColor = vec4(vec3(linearize_depth(texture2D(texture, tex).r)), 1);
}