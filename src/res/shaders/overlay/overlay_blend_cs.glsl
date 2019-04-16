#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba16f) uniform restrict readonly image2D scene_image;
layout (binding = 1, rgba16f) uniform restrict readonly image2D overlay_image;
layout (binding = 2, rgba16f) uniform writeonly image2D dest_image;

uniform ivec2 resolution;

uniform sampler2D depth_s;
uniform sampler2D depth_o;

const float zNear = 0.01f;
const float zFar = 100f;

//float linearize_depth(float d)
//{
//    float z  = d * 2.0 - 1.0;
//    return (2.0 * zNear) / (zFar + zNear - z * (zFar - zNear));
//}

float linearize_depth(float d)
{
    return (2.0 * zNear) / (zFar + zNear - d * (zFar - zNear));
}

void main() {

    vec3 dest;

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    vec2 uv = vec2(float(coord.x)/float(resolution.x), float(coord.y)/float(resolution.y));

    float depth_scene = texture(depth_s, uv).x;
    float depth_overlay = texture(depth_o, uv).x;

    vec3 ovr = imageLoad(overlay_image, coord).rgb;

    if(linearize_depth(depth_overlay) > linearize_depth(depth_scene) || length(ovr)==0){
        dest = imageLoad(scene_image, coord).rgb;
    } else {
        dest = ovr;
    }

    //dest = mix(dest, vec3(1-linearize_depth(depth_overlay)), 0.999f);

    imageStore(dest_image, coord, vec4(dest, 1));

}
