#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba16f) uniform readonly image2D albedo_image;
layout (binding = 1, rgba32f) uniform readonly image2D position_image;
layout (binding = 2, rgba32f) uniform readonly image2D normal_image;
layout (binding = 3) uniform writeonly image2D scene;

struct DirectionalLight {
    vec3 color;
    vec3 ambient;
    vec3 direction;
    float intensity;
};

struct Light {
    vec3 color;
    vec3 position;
    float intensity;
    int activated;
};

// position of camera
uniform vec3 camerapos;

// lights
uniform DirectionalLight sun;
uniform Light[20] lights;

// color to render if no geometry info
uniform vec3 clearColor;

// shadow map (as rendered from sun)
uniform sampler2D lightDepthMap;

// shadow view and ortho projection matrix
uniform mat4 lightSpaceMatrix;

// bias to counter shadow acne
const float shadowBias = 0.003f;

// 1: ssao on, 2: ssao off
uniform int ssao;

// total number of lights (excluding sun)
uniform int numLights;

const float PI = 3.14159265359;

float DistributionGGX(float NdotH, float roughness)
{
    float a      = roughness * roughness;
    float a2     = a * a;
    float NdotH2 = NdotH * NdotH;

    float num   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = PI * denom * denom;

    return num / denom;
}

float GeometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r)/8.0;

    float num   = NdotV;
    float denom = NdotV * (1.0 - k) + k;

    return num / denom;
}
float GeometrySmith(float NdotL, float NdotV, float roughness)
{
    float ggx2  = GeometrySchlickGGX(NdotV, roughness);
    float ggx1  = GeometrySchlickGGX(NdotL, roughness);

    return ggx1 * ggx2;
}
vec3 FresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

vec3 diffuseSpecular(vec4 albedo, vec3 N, vec3 V, vec3 F0,
                    float metal, float rough, vec3 lightDir, float intensity, vec3 color){

    vec3 L = normalize(lightDir);
    vec3 H = normalize(V+L);

    float NdotL = max(dot(N, L), 0.0);
    float NdotH = max(dot(N, H), 0.0);
    float NdotV = max(dot(N, V), 0.0);

    float NDF = DistributionGGX(NdotH, rough);
    float G   = GeometrySmith(NdotL, NdotV, rough);
    vec3 F    = FresnelSchlick(NdotH, F0);

    vec3 kS = F;
    vec3 kD = vec3(1.0) - kS;
    kD *= 1.0 - metal;

    vec3 numerator    =  G * NDF * F;
    float denominator = 4 * NdotV * NdotL;
    vec3 specular     = numerator / max(denominator, 0.001);
    float len = length(lightDir);

    vec3 ret;

    if(ssao == 1) ret =  (kD * albedo.rgb  / PI + specular)* NdotL * intensity * color/ (len*len);
    else ret = (kD * albedo.rgb / PI + specular)* NdotL * intensity * color/ (len*len);

    return ret;
    //return mix(vec3(G), ret, .00000001f);
}

float shadowFactor(vec3 position){

    vec4 lightSpaceCoord = lightSpaceMatrix * vec4(position,1.0);
    vec3 projCoords = lightSpaceCoord.xyz / lightSpaceCoord.w;
    projCoords = projCoords * 0.5 + 0.5;

    float currDepth = projCoords.z;

    float shadow = 0.0;
    vec2 texelSize = 1.0/textureSize(lightDepthMap, 0);

    for(int i = -1; i <= 1; i++){
        for(int j = -1; j <= 1; j++){
            float pcfDepth = texture(lightDepthMap, projCoords.xy + vec2(i,j) * texelSize).r;
            shadow += currDepth - shadowBias > pcfDepth ? 0.0 : 1.0;
        }
    }

    return shadow/9.0;

}

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    vec3 color;

    vec4 normal_and_metal = imageLoad(normal_image, coord);
    vec4 position_and_roughness = imageLoad(position_image, coord);

    vec3 normal = normal_and_metal.rgb;
    vec4 albedo = imageLoad(albedo_image, coord).rgba;

    if(normal.x < 0.1 && normal.x > -0.1 &&
    normal.y < 0.1 && normal.y > -0.1 &&
    normal.z < 0.1 && normal.z > -0.1 )
    {
        if(length(albedo.rgb) == 0) color = clearColor;
        else {
            color = albedo.rgb;
            color = color / (color + vec3(1.0));
            color = pow(color, vec3(1.0/2.2));
        }
    }
    else
    {
        vec3 position = position_and_roughness.rgb;
        float rough = position_and_roughness.a;
        float metal = normal_and_metal.a;

        //vec3 ao = imageLoad(ao_image, coord).rgb;

        if(rough < 0.02)
            rough = 0.02;

        vec3 N = normalize(normal);
        vec3 V = normalize(camerapos - position);

        vec3 F0 = vec3(.05);
        F0 = mix(F0, albedo.rgb, metal);

        float shadow = shadowFactor(position);

        vec3 Lo = shadow * diffuseSpecular(albedo, N, V, F0, metal, rough,
                                normalize(sun.direction), sun.intensity, sun.color);

        for(int i = 0; i<numLights; i++){
            if(lights[i].activated == 1)
                Lo += diffuseSpecular( albedo, N, V, F0, metal, rough,
                lights[i].position - position , lights[i].intensity, lights[i].color);

        }

        vec3 ambient = sun.ambient * albedo.rgb;
        //vec3 ambient = 0.0000001 * sun.ambient * albedo.rgb;
        color = Lo + ambient;

        color = color / (color + vec3(1.0));
        color = pow(color, vec3(1.0/2.2));

        //color = mix(color, diffuseSpecular(albedo, N, V, F0, ao.r, metal, rough,
         //                                                  normalize(sun.direction), sun.intensity, sun.color), .99f);
    }

    imageStore(scene, coord, vec4(color, 1));

 }