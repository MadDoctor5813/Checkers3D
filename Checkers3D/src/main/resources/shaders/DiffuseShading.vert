#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec4 color;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec2 uv;

uniform mat4 MVP;

out vec3 fragPos;
out vec4 fragColor;
out vec3 fragNormal;
out vec2 fragUV;

void main() {
    //transform position
    vec4 pos = MVP * vec4(pos, 1);
    fragPos = vec3(pos);
    gl_Position = pos;
    //pass through color
    fragColor = color;
    //transform the normals in shader
    fragNormal = normalize(transpose(inverse(mat3(MVP))) * normal);
    //pass through uv coords
    fragUV = uv;
}