#version 330

 layout (location = 0) in vec3 pos;

 uniform mat4 modelMatrix;
 uniform mat4 viewMatrix;
 uniform mat4 projectionMatrix;

 void main(){
     vec4 modelCoord = modelMatrix * vec4(pos, 1.0);
     gl_Position = projectionMatrix * viewMatrix * modelCoord;
 }