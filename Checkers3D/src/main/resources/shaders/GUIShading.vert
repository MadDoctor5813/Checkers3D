#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 uv;

out vec2 fragUv;

void main() {
    //no matrix here, because fullscreen rendering is in normalized device coords
    gl_Position = vec4(pos, 1);
    fragUv = uv;
}