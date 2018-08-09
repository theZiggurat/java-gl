#version 330

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

const int DIFFUSE = 0;
const int AMBIENT = 1;
const int SPECULAR = 2;
const int EMISSIVE = 3;
const int NORMAL = 4;
const int SPECULAR_EXPONENT = 5;
const int OPTICAL_DENSITY = 6;
const int DISSOLVE = 7;
const int PARAMETER_TYPES = 8;

in vec2 outTextureCoord;
in vec3 lightVertexNormal;
in vec3 lightVertexPos;
in mat4 outModelViewMatrix;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct SpotLight
{
    struct PointLight point;
    vec3 conedir;
    float cutoff;
};

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct MaterialParameter
{
    vec4 v_data;
    float f_data;
};

struct Material
{
    int texture_avalible[PARAMETER_TYPES];
    int illuminationModel;
};

uniform sampler2D diffuse_sampler;
uniform sampler2D ambient_sampler;
uniform sampler2D specular_sampler;
uniform sampler2D emissive_sampler;
uniform sampler2D normal_sampler;
uniform sampler2D specular_exponent_sampler;
uniform sampler2D optical_density_sampler;
uniform sampler2D dissolve_sampler;

uniform vec3 ambientLight;
uniform Material material;
uniform MaterialParameter parameters[PARAMETER_TYPES];
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;
vec4 emissiveC;

float specularExponentC;
float opticalC;
float dissolveC;

void setupColors(Material material, vec2 textureCoord)
{
    // diffuse
    if (material.texture_avalible[DIFFUSE] == 1)
        diffuseC = texture(diffuse_sampler, textureCoord);
    else diffuseC = parameters[DIFFUSE].v_data;

    // ambient
    if (material.texture_avalible[AMBIENT] == 1)
        ambientC = texture(ambient_sampler, textureCoord);
    else ambientC = parameters[AMBIENT].v_data;

    // specular
    if (material.texture_avalible[SPECULAR] == 1)
        speculrC = texture(specular_sampler, textureCoord);
    else speculrC = parameters[SPECULAR].v_data;

    // emissive
    if(material.texture_avalible[EMISSIVE] == 1)
      emissiveC = texture(emissive_sampler, textureCoord);
    else  emissiveC = parameters[EMISSIVE].v_data;

    // specular exponent
    if(material.texture_avalible[SPECULAR_EXPONENT] == 1)
        specularExponentC = texture(specular_exponent_sampler, textureCoord).r;
    else  specularExponentC = parameters[EMISSIVE].f_data;

    // optical density
    if(material.texture_avalible[OPTICAL_DENSITY] == 1)
        opticalC = texture(optical_density_sampler, textureCoord).r;
    else  opticalC = parameters[OPTICAL_DENSITY].f_data;

    // dissolve
    if(material.texture_avalible[DISSOLVE] == 1)
        dissolveC = texture(dissolve_sampler, textureCoord).r;
    else  dissolveC = parameters[DISSOLVE].f_data;

}

vec3 calcNormal(Material material, vec3 normal, vec2 text_coord, mat4 modelViewMatrix){
    vec3 ret = normal;
    if(material.texture_avalible[NORMAL] == 1){
        ret = texture(normal_sampler, text_coord).rgb;
        ret = normalize(ret * 2 - 1);
        ret = normalize(modelViewMatrix * vec4(ret, 0.0f)).xyz;
    }
    return ret;
}

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, parameters[SPECULAR_EXPONENT].f_data);
    specColor = speculrC * light_intensity  * specularFactor * vec4(light_color, 1f); // reflect map also here

    return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_color / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.point.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec3 from_light_dir  = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 color = vec4(0, 0, 0, 0);

    if ( spot_alfa > light.cutoff )
    {
        color = calcPointLight(light.point, position, normal);
        color *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
    }
    return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    setupColors(material, outTextureCoord);

    vec3 currNormal = calcNormal(material, lightVertexNormal, outTextureCoord, outModelViewMatrix);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, lightVertexPos, currNormal);
    for(int i = 0; i<MAX_POINT_LIGHTS; i++){
        if(pointLights[i].intensity>0){
            diffuseSpecularComp += calcPointLight(pointLights[i], lightVertexPos, currNormal);
        }
    }
    for(int i = 0; i<MAX_SPOT_LIGHTS; i++){
        if(spotLights[i].point.intensity>0){
            diffuseSpecularComp += calcSpotLight(spotLights[i], lightVertexPos, currNormal);
        }
    }

    fragColor = ambientC * vec4(ambientLight, 1.0f) + diffuseSpecularComp;
}