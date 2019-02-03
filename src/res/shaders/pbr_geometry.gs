#version 330

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

//out vec3 tangent_frag;

void main(){

    vec3 p1 = gl_in[0].gl_Position.xyz;
    vec3 p2 = gl_in[1].gl_Position.xyz;
    vec3 p3 = gl_in[2].gl_Position.xyz;

    vec3 t = normalize(p2-p1);
    vec3 b = cross(normalize(norm_vs), t);



    gl_Position = vec4(p1,1);
    EmitVertex();

    gl_Position = vec4(p2,1);
    EmitVertex();

    gl_Position = vec4(p3,1);
    EmitVertex();

    EndPrimitive();

}