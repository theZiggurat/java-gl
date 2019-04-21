#version 430

struct DirectionalLight {
    vec3 color;
    float ambient;
    vec3 direction;
    float intensity;
};

struct Light {
    vec3 color;
    vec3 position;
    float intensity;
    int activated;
};

layout (local_size_x = 16, local_size_y = 16) in;
layout (binding = 0, rgba16f) uniform restrict readonly image2DMS albedo_image;
layout (binding = 1, rgba32f) uniform restrict readonly image2DMS position_image;
layout (binding = 2, rgba32f) uniform restrict readonly image2DMS normal_image;
layout (binding = 3, r16f) uniform restrict readonly image2D ssao_image;
layout (binding = 4) uniform restrict writeonly image2DMS scene;

uniform vec3 camerapos;

uniform Light[20] lights;
uniform int numLights;

uniform DirectionalLight sun;
uniform sampler2D shadowDepthMap;
uniform mat4 shadowSpaceMatrix;

const float shadowBias = 0.0015f;
uniform int isShadow = 1;

uniform int ssao;
uniform float ssaoPower;

uniform int multisamples = 0;

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

vec3 diffuseSpecular(vec4 albedo, vec3 N, vec3 V, vec3 F0, float metal,
    float rough, float ao, vec3 lightDir, float lightIntensity, vec3 lightColor){

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
    vec3 diffuse = kD * albedo.rgb * ao / PI;

    return (diffuse + specular) * NdotL * lightIntensity * lightColor / (len*len);
}

float shadowFactor(vec3 position){

    vec4 shadowSpaceCoord = shadowSpaceMatrix * vec4(position,1.0);
    vec3 projCoords = shadowSpaceCoord.xyz / shadowSpaceCoord.w;
    projCoords = projCoords * 0.5 + 0.5;

    float currDepth = projCoords.z;

    float shadow = 0.0;
    vec2 texelSize = 1.0/textureSize(shadowDepthMap, 0);

    for(int i = -1; i <= 1; i++){
        for(int j = -1; j <= 1; j++){
            float pcfDepth = texture(shadowDepthMap, projCoords.xy + vec2(i,j) * texelSize).r;
            shadow += currDepth - shadowBias > pcfDepth ? 0.0 : 1.0;
        }
    }

    return shadow/9.0;

}

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    // OUT COLOR
    vec3 color = vec3(0);
    int i, samples = multisamples == 0 ? 1: multisamples;
    float ssao_factor = ssao == 1 ?
        pow(imageLoad(ssao_image, coord).r, ssaoPower) : 1;

    for(i = 0; i < samples; i++)
    {
        vec4 normal_and_metal = imageLoad(normal_image, coord, i);
        vec3 normal = normal_and_metal.rgb;

        if(normal == vec3(0))
        {
            color = vec3(1);//imageLoad(albedo_image, coord, 0).rgb;
            break;
        }
        else
        {
            vec4 albedo = imageLoad(albedo_image, coord, i);
            vec4 position_and_roughness = imageLoad(position_image, coord, i);
            vec3 position = position_and_roughness.rgb;
            float rough = position_and_roughness.a;
            float metal = normal_and_metal.a;

            //vec3 ao = imageLoad(ao_image, coord).rgb;

            if(rough < 0.02)
                rough = 0.02;

            vec3 N = normalize(normal);
            vec3 V = normalize(camerapos - position);

            vec3 F0 = vec3(.03);
            F0 = mix(F0, albedo.rgb, metal);

            float shadow = isShadow == 1 ? shadowFactor(position) : 1.;
            vec3 Lo = shadow * diffuseSpecular(albedo, N, V, F0, metal, rough, ssao_factor,
                                    normalize(sun.direction), sun.intensity, sun.color);

            for(int q = 0; q<numLights; q++){
                if(lights[q].activated == 1)
                    Lo += diffuseSpecular( albedo, N, V, F0, metal, rough, ssao_factor,
                    lights[q].position - position , lights[q].intensity, lights[q].color);

            }

            vec3 ambient = sun.ambient * albedo.rgb * ssao_factor;
            color += Lo + ambient;
        }
        color = color / (color + vec3(1.0));
        color = pow(color, vec3(1.0/2.15));

        //color = mix(color, imageLoad(albedo_image, coord, 0).rgb, 0.99);
        //color = mix(color, vec3(float(multisamples)/float(8)), 0.99f);

        imageStore(scene, coord, i, vec4(color, 1));
    }
 }