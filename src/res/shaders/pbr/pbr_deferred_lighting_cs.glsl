#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba16f) uniform readonly image2D albedo_image;
layout (binding = 1, rgba32f) uniform readonly image2D position_image;
layout (binding = 2, rgba32f) uniform readonly image2D normal_image;
layout (binding = 3, r16f) uniform readonly image2D metal_image;
layout (binding = 4, r16f) uniform readonly image2D rough_image;
layout (binding = 5, rgba32f) uniform readonly image2D ao_image;
layout (binding = 6) uniform writeonly image2D scene;



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

uniform vec3 camerapos;
uniform DirectionalLight sun;
uniform Light[20] lights;
uniform vec3 clearColor;

uniform sampler2D lightDepthMap;
uniform mat4 lightSpaceMatrix;
const float shadowBias = 0.003f;

uniform int ssao;

uniform int numLights;

const float PI = 3.14159265359;

float DistributionGGX(float NdotH, float roughness)
{
    float a      = roughness*roughness;
    float a2     = a*a;
    float NdotH2 = NdotH*NdotH;

    float num   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    //float denom = NdotH2 * a2 +(1-NdotH2);
    denom = PI * denom * denom;

    return num/denom;
}

float GeometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

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

vec3 diffuseSpecular(vec4 albedo, vec3 N, vec3 V, vec3 F0, float ao,
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

    vec3 numerator    = NDF * G * F;
    float denominator = 4 * NdotV * NdotL;
    vec3 specular     = numerator / max(denominator, 0.001);
    float len = length(lightDir);

    vec3 ret;

    if(ssao == 1) ret =  (kD * albedo.rgb * ao / PI + specular)* NdotL * intensity * color/ (len*len);
    else ret = (kD * albedo.rgb / PI + specular)* NdotL * intensity * color/ (len*len);

    //return ret;
    return mix(vec3(NdotH), ret, .99999999f);

//    if(ssao == 1)
//        return (kD * albedo.rgb * ao / PI + specular)* NdotL * intensity * color/ (len*len);
//    return (kD * albedo.rgb / PI + specular)* NdotL * intensity * color/ (len*len);
}

float shadowFactor(vec3 position){

    vec4 lightSpaceCoord = lightSpaceMatrix * vec4(position,1.0);
    vec3 projCoords = lightSpaceCoord.xyz / lightSpaceCoord.w;
    projCoords = projCoords * 0.5 + 0.5;

    float currDepth = projCoords.z;

    float shadow = 0.0;
    vec2 texelSize = 1.0/textureSize(lightDepthMap, 0);

    for(int i = -2; i <= 2; i++){
        for(int j = -2; j <= 2; j++){
            float pcfDepth = texture(lightDepthMap, projCoords.xy + vec2(i,j) * texelSize).r;
            shadow += currDepth - shadowBias > pcfDepth ? 0.0 : 1.0;
        }
    }

    return shadow/25.0;

}

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    vec3 color;

    vec3 normal = imageLoad(normal_image, coord).rgb;
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
        vec3 position = imageLoad(position_image, coord).rgb;

        float metal = imageLoad(metal_image, coord).x;
        float rough = imageLoad(rough_image, coord).x;
        vec3 ao = imageLoad(ao_image, coord).rgb;

        if(rough == 0.0)
            rough = 0.05;

        vec3 N = normalize(normal);
        vec3 V = normalize(camerapos - position);

        // vec3 FO = vec3((1 - IOR) / (1 + IOR));
        vec3 F0 = vec3(.05);
        F0 = mix(F0, albedo.rgb, metal);

        float shadow = shadowFactor(position);

        vec3 Lo = shadow * diffuseSpecular(albedo, N, V, F0, ao.r, metal, rough,
                                normalize(sun.direction), sun.intensity, sun.color);

        for(int i = 0; i<numLights; i++){
            if(lights[i].activated == 1)
                Lo += diffuseSpecular( albedo, N, V, F0, ao.r, metal, rough,
                lights[i].position - position , lights[i].intensity, lights[i].color);

        }

        vec3 ambient = sun.ambient * albedo.rgb;
        //vec3 ambient = 0.0000001 * sun.ambient * albedo.rgb;
        color = Lo + ambient;

        color = color / (color + vec3(1.0));
        color = pow(color, vec3(1.0/2.2));

        //color = mix(color, lights[0].position, .99f);
    }

    imageStore(scene, coord, vec4(color, 1));

 }