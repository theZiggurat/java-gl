#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba32f) uniform readonly image2D position_image;
layout (binding = 1, rgba32f) uniform readonly image2D normal_image;
layout (binding = 2) uniform writeonly image2D ssao_out;

uniform int resX;
uniform int resY;

uniform vec3[64] samples;
uniform int numSamples;

uniform vec3[16] randvec;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform float radius;

const float zfar = 10000f;
const float threshold = 0.02f;

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    vec3 worldpos = imageLoad(position_image, coord).rgb;
    vec3 worldnorm = normalize(imageLoad(normal_image, coord).rgb);

    vec3 viewpos = (viewMatrix * vec4(worldpos, 1)).xyz;
    vec3 viewnorm = normalize((viewMatrix * vec4(worldnorm, 0)).xyz);

    float depth = viewpos.z/zfar;

    ivec2 noiseCoord = coord - ivec2(floor(coord.x/4), floor(coord.y/4)) * 4;

    vec3 norm = viewnorm;

    vec3 rand = normalize(randvec[noiseCoord.x + noiseCoord.y*4]);
    vec3 tangent = normalize(rand - norm * dot(rand, norm));
    vec3 bitangent = cross(norm, tangent);
    mat3 tbn = mat3(tangent, bitangent, norm);

    float occlusion = 0.0;
    float occlusionOffset = 0.0;

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
            ivec2 offsetCoord = ivec2(offset.x * resX, offset.y*resY);
            vec3 offsetPos = imageLoad(position_image, offsetCoord).rgb;
            float sampleDepth = (viewMatrix * vec4(offsetPos,1.0)).z;

            float rangeCheck = abs(depth - sampleDepth) < threshold ? 1.0 : 0.0;
            occlusionOffset = (sampleDepth/zfar <= samp.z/zfar ? 1.0 : 0.0) * rangeCheck;
            occlusion += occlusionOffset;
        }
        else {
            occlusion += 0.4;
        }
    }

    vec3 color = vec3(1.0 -(occlusion/float(numSamples)));
    //color = mix(color, vec3(-depth), .99);
    //color = mix(color, viewpos, .99);

//    if(coord.x < 400)
//        color = mix(color, vec3(viewnorm), .99);
//    else if(coord.x < 800)
//        color = mix(color, vec3(tangent), .99);
//    else
//        color = mix(color, vec3(bitangent), .99);
//

    if(length(worldpos) == 0)
        imageStore(ssao_out, coord, vec4(1,1,1,1));
    else
        imageStore(ssao_out, coord, vec4(color,1));


}