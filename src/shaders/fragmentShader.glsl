#version 400 core

in vec2 pass_textureCoords;
flat in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
flat in vec3 pass_color;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.2);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float specularVector = dot(reflectedLightDirection, unitVectorToCamera);
	specularVector = max(specularVector, 0.2);
	float dampedVector = pow(specularVector, shineDamper);
	vec3 finalSpecular = dampedVector * reflectivity * lightColor;

	out_Color = vec4(diffuse, 1.0) * vec4(pass_color, 1.0) + vec4(finalSpecular, 1.0);

	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);

}
