#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler3;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
flat in vec2 texCoord0;
flat in vec2 texCoord0End;
in vec2 texCoord1;
in vec4 lightMapColor;
in vec4 vertexColor;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 shiftColor = texture(Sampler3, texCoord1);
    if (shiftColor.a < 0.1) {
        discard;
    }
    vec2 texCoord2 = texCoord0 + shiftColor.rg * (texCoord0End - texCoord0);
    vec4 color = texture(Sampler0, texCoord2) * vertexColor * ColorModulator * lightMapColor;
    if (color.a < 0.05) {
        discard;
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
