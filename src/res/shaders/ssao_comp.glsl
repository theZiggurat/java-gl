#version 430

layout (local_size_x = 16, local_size_y = 16) in;
layout (binding = 0, rgba16f) uniform writeonly image2D ssao_out;
layout (binding = 1, rgba32f) uniform readonly image2D position_image;
layout (binding = 2, rgba32f) uniform readonly image2D normal_image;

uniform int resX;
uniform int resY;


uniform sampler2D noise;

uniform vec3[64] samples;
uniform int numSamples;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform float z_far;

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    vec4 worldPosition = imageLoad(position_image, coord);
    vec4 normalWorld = imageLoad(normal_image, coord);
    vec3 normal = normalize((viewMatrix * normalWorld).rgb);
    vec3 pos = (viewMatrix * worldPosition).rgb;

    vec3 rand = texelFetch(noise, coord, 0).rgb;

    //Gram–Schmidt process to find TBN
    vec3 tangent = normalize(rand - normal * (dot(normal, rand)));
    vec3 bitangent = cross(normal, tangent);
    mat3 tbn = mat3(tangent, bitangent, normal);

    float occlusionSum = 0;
    for(int i = 0; i < numSamples; i++){

        vec3 samp = tbn * samples[i];
        samp += pos;

        vec4 sampleCheck = projectionMatrix * vec4(samp, 1);
        sampleCheck.xy /= sampleCheck.w;
        sampleCheck.xy = sampleCheck.xy * 0.5 + 0.5;

        if(sampleCheck.x >=0 && sampleCheck.x<1.0 && sampleCheck.y >=0 && sampleCheck.y < 1.0){

            ivec2 offsetCoord = ivec2(sampleCheck.x * resX, sampleCheck.y * resY);
            vec4 offsetWorld = imageLoad(position_image, offsetCoord);
            float offsetDepth = (viewMatrix * vec4(offsetWorld.rgb, 0)).z;

            occlusionSum += pos.z/z_far <= offsetDepth/z_far ? 1:0;

        } else {
            occlusionSum += 0.4;
        }
    }

    occlusionSum /= numSamples;

    coord.y = coord.y*2-1;
    imageStore(ssao_out, coord, vec4(occlusionSum, occlusionSum, occlusionSum, 1));

}