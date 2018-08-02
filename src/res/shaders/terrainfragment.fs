#version 330

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

const vec3 UP = vec3(0,1,0);

in vec2 outTextureCoord;
in vec3 lightVertexNormal;
in vec3 lightVertexPos;
in vec3 cliffShear;

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


uniform sampler2D texture_sampler_grass;
uniform sampler2D texture_sampler_stone;

uniform vec3 ambientLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;
uniform float specularPower;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColors(vec2 textureCoord)
{
    float grassComp;

    grassComp = abs(dot(normalize(cliffShear), UP))/1.2;

    vec4 grassColor = texture(texture_sampler_grass, textureCoord);
    vec4 stoneColor = texture(texture_sampler_stone, textureCoord);

    ambientC = mix(stoneColor,grassColor, grassComp);
    diffuseC = ambientC;
    speculrC = ambientC;
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
    specularFactor = pow(specularFactor, specularPower);
    specColor = speculrC * light_intensity  * specularFactor *  vec4(light_color, 1.0);

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

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 position, vec3 normal)
{
    return calcLightColor(directionalLight.color, directionalLight.intensity,
                        position, normalize(directionalLight.direction), normal);
}

void main()
{
    setupColors(outTextureCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, lightVertexPos, lightVertexNormal);
    for(int i = 0; i<MAX_POINT_LIGHTS; i++){
        if(pointLights[i].intensity>0){
            diffuseSpecularComp += calcPointLight(pointLights[i], lightVertexPos, lightVertexNormal);
        }
    }
    for(int i = 0; i<MAX_SPOT_LIGHTS; i++){
        if(spotLights[i].point.intensity > 0){
            diffuseSpecularComp += calcSpotLight(spotLights[i], lightVertexPos, lightVertexNormal);
        }
    }

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp +
                (0.000001f*specularPower);
}