#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba16f) uniform readonly image2D scene_image;
layout (binding = 1, rgba16f) uniform readonly image2D overlay_image;
layout (binding = 2, rgba16f) uniform writeonly image2D dest_image;

void main() {

    vec3 dest;

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    vec3 overlay_color = imageLoad(overlay_image, coord).rgb;

    if(overlay_color!=vec3(0,0,0)){
        dest = overlay_color;
    } else {
        dest = imageLoad(scene_image, coord).rgb;
    }

    imageStore(dest_image, coord, vec4(dest, 1));

}
