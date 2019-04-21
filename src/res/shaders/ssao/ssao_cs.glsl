#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba32f) uniform restrict readonly image2DMS position_image;
layout (binding = 1, rgba32f) uniform restrict readonly image2DMS normal_image;
layout (binding = 2) uniform writeonly image2D ssao_out;

uniform ivec2 resolution;

uniform vec3[64] samples;
uniform int numSamples;

uniform float radius;

uniform vec3[16] randvec;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

const float zFar = 10000f;
const float zNear = 0.01f;
const float bias = 0.1f;

float linearize_depth(float d)
{
    return (2.0 * zNear) / (zFar + zNear - d * (zFar - zNear));
}

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    vec3 worldpos = imageLoad(position_image, coord, 0).rgb;
    vec3 worldnorm = normalize(imageLoad(normal_image, coord, 0).rgb);

    vec3 viewpos = (viewMatrix * vec4(worldpos, 1)).xyz;
    vec3 viewnorm = normalize((viewMatrix * vec4(worldnorm, 0)).xyz);

    float depth = viewpos.z;

    ivec2 noiseCoord = coord - ivec2(floor(coord.x/4), floor(coord.y/4)) * 4;

    vec3 norm = viewnorm;
    vec3 rand = normalize(randvec[noiseCoord.x + noiseCoord.y*4]);
    vec3 tangent = normalize(rand - norm * dot(rand, norm));
    vec3 bitangent = cross(norm, tangent);
    mat3 tbn = mat3(tangent, bitangent, norm);

    float occlusion = 0.0;
    for(int i = 0; i < numSamples; i++)
    {
        vec3 samp = tbn * samples[i];
        samp = samp * radius + viewpos;

        vec4 offset = vec4(samp, 1.0);
        offset = projectionMatrix * offset;
        offset.xy /= offset.w;
        offset.xy = offset.xy * 0.5 + 0.5;

        if(offset.x < 1.0 && offset.x >= 0.0 && offset.y < 1.0 && offset.y >= 0.0)
        {
            ivec2 offsetCoord = ivec2(offset.x * resolution.x, offset.y * resolution.y);
            vec3 offsetPos = imageLoad(position_image, offsetCoord, 0).rgb;
            float sampleDepth = (viewMatrix * vec4(offsetPos,1.0)).z;

            float rangeCheck = smoothstep(0.0, 1.0, radius / abs(depth - sampleDepth));
            occlusion += (sampleDepth >= samp.z + bias ? 1.0 : 0.0) * rangeCheck;
        }
    }

    occlusion /= float(numSamples);

    if(length(worldpos) == 0)
        imageStore(ssao_out, coord, vec4(1,1,1,1));
    else
        imageStore(ssao_out, coord, vec4(1-vec3(occlusion),1));


}