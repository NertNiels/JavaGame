#version 400 core

const vec4 waterColor = vec4(0.604, 0.867, 0.851, 1);
const float colorDepth = 15;
const float minBlueness = 0.4;
const float maxBlueness = 0.75;
const float gradient = 15;
const float frenselReflective = 0.5;
const float edgeSoftness = 1;

out vec4 out_Color;

in vec4 pass_clipSpaceGrid;
in vec4 pass_clipSpaceReal;
in vec3 pass_toCameraVector;
in vec4 pass_positionRelativeToCamera;
flat in vec3 pass_normal;
flat in vec4 pass_specular;
flat in vec4 pass_diffuse;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform sampler2D skyBoxTexture;
uniform float density;

float smoothlystep(float edge0, float edge1, float x) {
  x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
  return x * x * (3 - 2 * x);
}

float toLinearDepth(float zDepth) {
	float near = 0.1;
	float far = 1000;
	return 1.0 * near * far / (far + near - (2.0 * zDepth - 1.0) * (far - near));
}

float calculateWaterDepth(vec2 texCoords) {
	float depth = texture(depthTexture, texCoords).r;
	float floorDistance = toLinearDepth(depth);
	depth = gl_FragCoord.z;
	float waterDistance = toLinearDepth(depth);
	return floorDistance - waterDistance;
}

float frensel(vec3 n, vec3 toCamVector) {
	vec3 viewVector = normalize(toCamVector);
	vec3 normal = normalize(n);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, frenselReflective);
	return clamp(refractiveFactor, 0.0, 1.0);
}

vec2 clipSpaceToTexCoords(vec4 clipSpace) {
	vec2 ndc = clipSpace.xy / clipSpace.w;
	vec2 texCoords = ndc / 2.0 + 0.5;
	return clamp(texCoords, 0.002, 0.998);
}

vec4 applyDepthColor(vec4 refractColor, float waterDepth) {
	float depthFactor = smoothlystep(0, colorDepth, waterDepth);
	float colorFactor = minBlueness + depthFactor * (maxBlueness - minBlueness);
	return mix(refractColor, waterColor, colorFactor);
}

float calculateVisibility(vec3 toCameraVector) {
	float distance = length(toCameraVector);
	float visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
	return visibility;
}

void main(void) {

	vec2 texCoordsReal = clipSpaceToTexCoords(pass_clipSpaceReal);
	vec2 texCoordsGrid = clipSpaceToTexCoords(pass_clipSpaceGrid);

	vec2 refractionTexCoords = texCoordsGrid;
	vec2 reflectionTexCoords = vec2(texCoordsGrid.x, 1.0 - texCoordsGrid.y);
	float waterDepth = calculateWaterDepth(texCoordsReal);

	vec4 refractColor = texture(refractionTexture, refractionTexCoords);
	vec4 reflectColor = texture(reflectionTexture, reflectionTexCoords);

	refractColor = applyDepthColor(refractColor, waterDepth);
	reflectColor = mix(reflectColor, waterColor, minBlueness);

	vec4 finalColor = mix(reflectColor, refractColor, frensel(pass_normal, pass_toCameraVector));
	finalColor = finalColor * pass_diffuse + pass_specular;

	out_Color = finalColor;
	out_Color.a = clamp(waterDepth / edgeSoftness, 0., 1.);
	out_Color = mix(texture(skyBoxTexture, texCoordsGrid), out_Color, calculateVisibility(pass_positionRelativeToCamera.xyz));

}
