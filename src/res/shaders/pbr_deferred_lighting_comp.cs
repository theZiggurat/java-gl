#version 430

layout (local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba16f) uniform readonly image2D albedo_image;
layout (binding = 1, rgba32f) uniform readonly image2D position_image;
layout (binding = 2, rgba32f) uniform readonly image2D normal_image;
layout (binding = 3, r16f) uniform readonly image2D metal_image;
layout (binding = 4, r16f) uniform readonly image2D rough_image;
layout (binding = 5) uniform writeonly image2D scene;

uniform vec3 cameraPos;

uniform vec3 light_dir;

const float PI = 3.14159265359;

float DistributionGGX(vec3 N, vec3 H, float roughness)
{
    float a      = roughness*roughness;
    float a2     = a*a;
    float NdotH  = max(dot(N, H), 0.0);
    float NdotH2 = NdotH*NdotH;

    float num   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = PI * denom * denom;

    return num / denom;
}

float GeometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float num   = NdotV;
    float denom = NdotV * (1.0 - k) + k;

    return num / denom;
}
float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness)
{
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2  = GeometrySchlickGGX(NdotV, roughness);
    float ggx1  = GeometrySchlickGGX(NdotL, roughness);

    return ggx1 * ggx2;
}
vec3 fresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

void main(){

    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);

    vec4 albedo = imageLoad(albedo_image, coord).rgba;
    vec3 position = imageLoad(position_image, coord).rgb;
    vec3 normal = imageLoad(normal_image, coord).rgb;
    float metal = imageLoad(metal_image, coord).r;
    float rough = imageLoad(rough_image, coord).r;



    vec3 N = normalize(normal);
    vec3 V = normalize(cameraPos - position);

    vec3 F0 = vec3(.01);
    F0 = mix(F0, albedo.rgb, metal);

    vec3 L = normalize(light_dir);
    vec3 H = normalize(V+L);

    float NDF = DistributionGGX(N, H, rough);
    float G   = GeometrySmith(N, V, L, rough);
    vec3 F    = fresnelSchlick(max(dot(H, V), 0.0), F0);

    vec3 Lo = vec3(0,0,0);

    vec3 kS = F;
    vec3 kD = vec3(1.0) - kS;
    kD *= 1.0 - metal;

    vec3 numerator    =  NDF*G* F;
    float denominator = 4* max(dot(N, V), 0) * max(dot(N, L), 0);
    vec3 specular     = numerator / max(denominator, 0.01);

    float NdotL = max(dot(N, L), 0.0);
    Lo += (kD * albedo.xyz / PI + specular)*2* NdotL;

    vec3 ambient = vec3(0.05) * albedo.rgb;
    vec3 color = Lo + ambient;


    color = color / (color + vec3(1.0));
    color = pow(color, vec3(1.0/2.2));

    if(length(normal) == 0){
        color = vec3(0, .1, .15);
    }

    imageStore(scene, coord, vec4(color, 1));

 }