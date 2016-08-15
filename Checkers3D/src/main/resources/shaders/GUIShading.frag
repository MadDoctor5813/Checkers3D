#version 330

in vec2 fragUv;

out vec4 fragColor;

uniform sampler2D tex;

void main() {
    //the color is just the texture lookup
    fragColor = vec4(texture(tex, fragUv));
}