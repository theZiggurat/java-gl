#version 330

struct Box {
    float x;
    float y;
    float width;
    float height;
};

in vec3 pos;
out vec2 uv;
uniform Box box;

void main() {

    vec2 coord = vec2(box.x + box.width * pos.x, box.y + box.height * pos.y);
    gl_Position = vec4(coord*2 - 1, 0, 1);

    uv = pos.xy;
}