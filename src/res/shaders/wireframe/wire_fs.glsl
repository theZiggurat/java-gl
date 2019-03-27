#version 330

layout (location = 2) out vec4 fragColor;
uniform float time;

void main(){

    float red = sin(time*0.5)*cos(time*2);
    float green = sin(time*0.2)*cos(time*1.3);
    float blue = sin(time*2)*cos(time*2);

    vec3 color = vec3(red, green, blue);

    fragColor = vec4(color, 1);
}