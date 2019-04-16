#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, r16f) uniform readonly image2D ssao_image;
layout (binding = 1) uniform writeonly image2D out_image;

uniform ivec2 resolution;

const int blurSize = 4;

void main() {

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    float kernelSize = float(blurSize*blurSize);

    float result = 0.0;
    ivec2 edge = ivec2(-blurSize/2.0);

    for(int i = 0; i < blurSize; i++){
        for(int j = 0; j < blurSize; j++){
            ivec2 offset = edge + ivec2(i,j);
            ivec2 sampleCoord = offset + coord;

            if(sampleCoord.x >= 0 && sampleCoord.y >= 0 &&
            sampleCoord.x < resolution.x && sampleCoord.y < resolution.y )
                result += imageLoad(ssao_image, sampleCoord).r;
             else
                kernelSize--;

        }
    }

    result/=kernelSize;
    imageStore(out_image, coord, vec4(vec3(result), 1));

}
