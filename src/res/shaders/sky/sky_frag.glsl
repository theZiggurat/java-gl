# version 430

layout (location = 3) out vec4 scene_vbo;

in vec3 fragPos;
in float height;
uniform float scale;


void main(){

    float red =  0.15 * (height+1400) / scale + 0.12;
	float green =  0.23 * (height+1400) / scale + 0.25;
	float blue =  0.5 * (height+1400) / scale + 0.47;
    scene_vbo = mix(vec4(fragPos, 1), vec4(red, green, blue, 1), 0.99);
}