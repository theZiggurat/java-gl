#version 430

layout (local_size_x = 1, local_size_y = 1) in;
layout (binding = 0) uniform writeonly image2D noise_out;

uniform vec2 noise_vec2[16];

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    vec3 color = vec3(noise_vec2[coord.x*4+coord.y], 0);
    imageStore(noise_out, coord, vec4(color,1));
}