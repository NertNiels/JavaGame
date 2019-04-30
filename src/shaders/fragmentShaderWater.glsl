#version 400 core

in vec4 out_color_reflect;
in vec4 out_color_refract;
in vec4 out_color_skyBox;
in float out_waterBottom;
in float out_frensel;
in float out_visibility;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform vec3 skyColor;

const vec4 waterColor = vec4(0.0, 0.5, 0.7, 1.0);
const float colorDepth = 15;
const float minBlueness = 0.4;
const float maxBlueness = 0.75;

float smoothlystep(float edge0, float edge1, float x) {
  x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
  return x * x * (3 - 2 * x);
}

float toLinearDepth(float zDepth) {
	float near = 0.1;
	float far = 1000;
	return 1.0 * near * far / (far + near - (2.0 * zDepth - 1.0) * (far - near));
}

float calculateWaterDepth(vec2 texCoords, float depth) {
	float floorDistance = toLinearDepth(depth);
	depth = gl_FragCoord.z;
	float waterDistance = toLinearDepth(depth);
	return floorDistance - waterDistance;
}

vec4 applyDepthColor(vec4 refractColor, float waterDepth) {
	float depthFactor = smoothlystep(0, colorDepth, waterDepth);
	float colorFactor = minBlueness + depthFactor * (maxBlueness - minBlueness);
	return mix(refractColor, waterColor, colorFactor);
}

void main(void) {
	vec4 refractColor = applyDepthColor(out_color_refract, out_waterBottom);
	out_Color = mix(out_color_reflect, refractColor, out_frensel);

	out_Color = mix(out_color_skyBox, out_Color, out_visibility);
}
