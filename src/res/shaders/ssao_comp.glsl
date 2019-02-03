#version 430

layout (local_size_x = 32, local_size_y = 32) in;

layout (binding = 0, rgba32f) uniform readonly image2D position_image;
layout (binding = 1, rgba32f) uniform readonly image2D normal_image;
layout (binding = 2, rgba32f) uniform readonly image2D noise_image;
layout (binding = 3) uniform writeonly image2D ssao_out;

//uniform int resX;
//uniform int resY;

//uniform sampler2D depthMap;

uniform vec3[64] samples;
uniform int numSamples;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

const float radius = 1.0;

//uniform float z_far;

float linearize_depth(float depth){
    return (2.0 * 0.01 * 10000) / (10000 + 0.01 - depth * (10000 - 0.01));
}

void main(){

    vec3 viewNormal;
    vec3 viewPos;
    vec3 tangent;
    vec3 bitangent;
    vec4 color;
    vec3 rand;
    vec3 random;

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    ivec2 randcoord = coord - ivec2(floor(gl_GlobalInvocationID.x/4), floor(gl_GlobalInvocationID.y/4)) * 4;

    vec3 worldPosition = imageLoad(position_image, coord).xyz;
    vec3 worldNormal = normalize(imageLoad(normal_image, coord).rgb);

    float rangeCheck = 0;
    float offsetDepth = 0;

    if (length(worldNormal) < .95){
            color = vec4(1);
    } else {

        viewPos = (viewMatrix * vec4(worldPosition,1)).xyz;
        viewNormal = normalize(viewMatrix * vec4(worldNormal.rgb, 0)).xyz;
        viewNormal = viewNormal*.5 + vec3(.5);

        rand = imageLoad(noise_image, randcoord).rgb;

        //Gram–Schmidt process to find TBN
        tangent = normalize(rand - viewNormal * dot(viewNormal, rand));
        bitangent = cross(viewNormal, tangent);
        mat3 tbn = mat3(tangent, bitangent, viewNormal);

        vec4 offsetWorld;

        float occlusionSum = 0;
        for(int i = 0; i < numSamples; i++){

            vec3 samp = tbn * samples[i];
            samp = viewPos + (samp*radius);

            vec4 sampleCheck = projectionMatrix * vec4(samp, 1);
            sampleCheck.xy /= sampleCheck.w;
            sampleCheck.xy = sampleCheck.xy * 0.5 + 0.5;
            sampleCheck.xy  = 1-sampleCheck.xy;

            if(sampleCheck.x >=0.0 && sampleCheck.x<1.0 && sampleCheck.y >=0.0 && sampleCheck.y < 1.0){

                ivec2 offsetCoord = ivec2(sampleCheck.x * 1280, sampleCheck.y * 720);
                offsetDepth = (viewMatrix * vec4(imageLoad(position_image, coord).rgb, 1.0)).z;

                rangeCheck = smoothstep(0.0, 1.0, radius/abs(viewPos.z - offsetDepth));
                occlusionSum += (offsetDepth/10000 >= samp.z/10000 ? 1.0:0.0) * rangeCheck;

            } else {
                occlusionSum += 0.4f;
            }
        }

        occlusionSum /= float(numSamples);

        color = vec4(vec3(1-occlusionSum), 1);

        //color = mix(color, vec4, .9);

        //color = mix(color, vec4(viewNormal,1), .99);
        //color = mix(color, vec4(randcoord, 0,1),.9999);
    }


    imageStore(ssao_out, coord, color);

}