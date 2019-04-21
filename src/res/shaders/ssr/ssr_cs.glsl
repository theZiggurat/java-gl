#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba32f) uniform restrict readonly image2DMS positionImage;
layout (binding = 1, rgba32f) uniform restrict readonly image2DMS normalImage;
layout (binding = 2, r16f) uniform restrict readonly image2DMS aoImage;
layout (binding = 3, rgba16f) uniform restrict image2DMS outImage;

uniform ivec2 resolution;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

const float minRayStep = 0.1;

uniform int raymarchSteps = 30;
uniform int binarySearchSteps = 5;
uniform float rayStepLen = 0.1;
uniform float falloffExp = 3.;
uniform int sampleCount = 1;

vec4 raymarch(in vec3 direction, inout vec3 hitCoord, out float dDepth);
vec3 binarySearch(inout vec3 direction, inout vec3 hitCoord, inout float dDepth);
vec3 fresnelSchlick(float cosTheta, vec3 F0);
vec3 hash(vec3 a);

void main() {

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    vec4 normal_metal = imageLoad(normalImage, coord, 0);
    if(normal_metal.w < 0.02) return;

    vec4 position_rough = imageLoad(positionImage, coord, 0);
    if(position_rough.w > 0.8) return;

    vec3 viewPos = (viewMatrix * vec4(position_rough.rgb,1)).xyz;
    vec3 viewNorm = normalize((viewMatrix*vec4(normal_metal.rgb,0)).xyz);

    vec3 lastFrame = imageLoad(outImage, coord, 0).rgb;

    vec3 F0 = vec3(0.03);
    F0 = mix(F0, lastFrame, normal_metal.w);
    vec3 fresnel  = fresnelSchlick(max(dot(viewNorm, normalize(viewPos)), 0.0), F0);

    vec3 reflected = normalize(reflect(normalize(viewPos), viewNorm));

    vec3 hitPos = viewPos;
    float dDepth;

    vec3 ssrColor = vec3(0);
    float reflectionFac = 0.;

    for(int i = 0; i < sampleCount; i++)
    {
        vec3 jitt = mix(vec3(0.0), vec3(hash(position_rough.xyz+vec3(float(i)))), position_rough.w);
        vec4 ssrUV = raymarch((vec3(jitt) + reflected * max(minRayStep, -viewPos.z)), hitPos, dDepth);
        ivec2 ssrCoord = ivec2(ssrUV.x * resolution.x, ssrUV.y * resolution.y);

        vec2 dCoords = smoothstep(0.2, 0.6, abs(vec2(0.5, 0.5) - ssrUV.xy));
        float screenEdgeFactor = clamp(1.0 - (dCoords.x + dCoords.y), 0.0, 1.0);


        float reflectionMult = pow(normal_metal.w, falloffExp) *
                                screenEdgeFactor * -reflected.z;
        reflectionMult = clamp(reflectionMult, 0., 0.9);

        ssrColor += imageLoad(outImage, ssrCoord, 0).rgb * reflectionMult;
        reflectionFac += reflectionMult;
    }
    ssrColor /= sampleCount;
    reflectionFac /= sampleCount;

    float ao = imageLoad(aoImage, coord, 0).r;

    vec3 currColor = imageLoad(outImage, coord, 0).rgb;
    vec3 color = mix(currColor, ssrColor, normal_metal.w * reflectionFac)*ao;
    //color = mix(color, vec3(reflectionMult), 0.999);
    imageStore(outImage, coord, 0, vec4(color,1));

}

vec3 fresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

vec4 raymarch(in vec3 direction, inout vec3 hitCoord, out float dDepth)
{
    direction *= rayStepLen;

    float depth = 0.0;
    int steps = 0;

    vec4 projectedCoord = vec4(0.0);
    ivec2 samplePos;

    for(int i = 0; i < raymarchSteps; ++i){
        hitCoord += direction;

        projectedCoord = projectionMatrix * vec4(hitCoord, 1.0);
        projectedCoord.xy /= projectedCoord.w;
        projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;

        samplePos = ivec2(projectedCoord.x * resolution.x, projectedCoord.y * resolution.y);
        vec4 wp = imageLoad(positionImage, samplePos, 0);
        depth = (viewMatrix * vec4(wp.xyz, 1)).z;

        if(depth > 1)
            continue;

        dDepth = hitCoord.z - depth;

        if(dDepth <= 0 && (direction.z - dDepth < 2))
            return vec4(binarySearch(direction, hitCoord, dDepth), 1.0);

        steps++;
    }
    return vec4(projectedCoord.xy, depth, 0.0);
}

vec3 binarySearch(inout vec3 direction, inout vec3 hitCoord, inout float dDepth)
{
    float depth;
    vec4 projectedCoord;

    for(int i = 0; i < binarySearchSteps; i++)
    {
        projectedCoord = projectionMatrix * vec4(hitCoord, 1.0);
        projectedCoord.xy /= projectedCoord.w;
        projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;

        ivec2 samplePos = ivec2(projectedCoord.x * resolution.x, projectedCoord.y * resolution.y);
        vec4 wp = imageLoad(positionImage, samplePos, 0);
        depth = (viewMatrix * vec4(wp.xyz, 1)).z;

        dDepth = hitCoord.z - depth;

        direction *= 0.5;
        if(dDepth > 0.0)
            hitCoord += direction;
        else
            hitCoord -= direction;
    }

    projectedCoord = projectionMatrix * vec4(hitCoord, 1.0);
    projectedCoord.xy /= projectedCoord.w;
    projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;

    return vec3(projectedCoord.xy, depth);
}

vec3 hash(vec3 a)
{
    a = fract(a * vec3(0.8, 0.8, 0.8));
    a += dot(a, a.yxz + 19.19);
    return fract((a.xxy + a.yxx)*a.zyx);
}
