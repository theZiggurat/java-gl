# version 330

in vec2 uv_frag;
in vec3 norm_frag;
in vec3 pos_frag;

out vec4 fragColor;

//uniform sampler2D albedoMap;

void main(){
    fragColor = vec4(norm_frag,1.0);
}