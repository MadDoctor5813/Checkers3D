#version 330

layout(location = 0) in vec3 pos;

uniform mat4 VP;

void main() {
    //multiply the position by the view projection matrix for the final position
    gl_Position = VP * vec4(pos, 1);
}