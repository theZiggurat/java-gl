# version 330

 layout (location = 0) in vec3 pos;
 layout (location = 1) in vec2 uv;
 layout (location = 2) in vec3 norm;

 out VS_DATA{
     vec2 uv;
     vec3 norm;
     vec3 pos;
 } vs;

 uniform mat4 modelMatrix;
 uniform mat4 viewMatrix;
 uniform mat4 projectionMatrix;

 void main(){
     vec4 modelCoord = modelMatrix * vec4(pos, 1.0);
     gl_Position = projectionMatrix * viewMatrix * modelCoord;
     vs.pos = modelCoord.xyz;
     vs.uv = vec2(uv.x, uv.y);
     vs.norm = normalize(modelMatrix * vec4(norm, 0)).xyz;
 }