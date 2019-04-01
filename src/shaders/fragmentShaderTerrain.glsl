#version 400 core

flat in vec4 pass_color;
flat in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in vec4 clipSpaceCoords;
in float visibility;

out vec4 out_Color;

uniform sampler2D skyBoxTexture;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

vec2 clipSpaceToTexCoords(vec4 clipSpace) {
	vec2 ndc = clipSpace.xy / clipSpace.w;
	vec2 texCoords = ndc / 2.0 + 0.5;
	return clamp(texCoords, 0.002, 0.998);
}

void main(void) {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.2);
	vec3 diffuse = brightness * lightColor;


	out_Color = vec4(diffuse, 1.0) * pass_color;
	out_Color = mix(texture(skyBoxTexture, clipSpaceToTexCoords(clipSpaceCoords)), out_Color, visibility);
}
