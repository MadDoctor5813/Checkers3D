#version 330

in vec3 fragPos;
in vec4 fragColor;
in vec3 fragNormal;
in vec2 fragUV;

out vec4 outColor;

uniform sampler2D tex;

uniform vec3 specColor;
uniform float shininess;

struct Light {
    vec3 pos;
    vec3 color;
    float ambientFac;
    float attenuationFac;
};

uniform Light light;
uniform vec3 camPos;

void main() {
    vec4 texColor = texture(tex, vec2(fragUV.x, 1 - fragUV.y));
    //if the alpha value is below 0.5, this fragment is transparent and should not be rendered
    if (texColor.a < 0.5) {
        //discard just means throw out the fragment and exit
        discard;
    }
    vec3 toLight = normalize(light.pos - fragPos);
    vec3 toCam = normalize(camPos - fragPos);

    vec3 ambient = light.ambientFac * texColor.rgb;

    //calculate diffuse component based on the angle from the pixel to the light
    vec3 diffuse = max(0.0, dot(fragNormal, toLight)) * texColor.rgb * light.color;

    //calculate specular component based on angle from the camera and the reflection of the light of the pixel
    float specCoefficient = pow(max(0.0, dot(toCam, reflect(-toLight, fragNormal))), shininess);
    vec3 specular = specCoefficient * specColor * light.color;

    //calculate attenuation based on the distance from the light to the pixel
    float attenuation = 1 / (1 + light.attenuationFac * pow(length(light.pos - fragPos), 2));
    //add it all together
    vec3 linear = ambient + (attenuation * (diffuse + specular));
    //gamma correction
    outColor = vec4(pow(linear, vec3(1/2.2)), texColor.a);
}

