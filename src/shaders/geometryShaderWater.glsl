#version 400 core

layout ( triangles ) in;
layout ( triangle_strip, max_vertices = 3 ) out;

in vec3 pass_toCameraVector[];
in float pass_visibility[];

out vec4 out_color_reflect;
out vec4 out_color_refract;
out vec4 out_color_skyBox;
out float out_visibility;
out float out_waterBottom;
out float out_frensel;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform sampler2D skyBoxTexture;
uniform float density;

const float gradient = 15;
const float frenselReflective = 0.9;
const vec4 waterColor = vec4(0.0, 0.5, 0.7, 1.0);

float smoothlystep(float edge0, float edge1, float x) {
	x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
	return x * x * (3 - 2 * x);
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

vec4 getBlurredColor(sampler2D tex, vec2 coords) {
	vec4 c = texture(tex, coords - vec2(-1, -1)) * 0.052356;
	c += texture(tex, coords - vec2(0, -1)) * 0.124103;
	c += texture(tex, coords - vec2(1, -1)) * 0.052356;
	c += texture(tex, coords - vec2(-1, 0)) * 0.124103;
	c += texture(tex, coords - vec2(0, 0)) * 0.294168;
	c += texture(tex, coords - vec2(1, 0)) * 0.124103;
	c += texture(tex, coords - vec2(-1, 1)) * 0.052356;
	c += texture(tex, coords - vec2(0, 1)) * 0.124103;
	c += texture(tex, coords - vec2(1, 1)) * 0.052356;
	return c;
}


float calculateVisibility(vec3 toCameraVector) {
	float distance = length(toCameraVector);
	float visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
	return visibility;
}

void main(void) {
	vec4 firstVertex = gl_in[0].gl_Position;
	vec4 secondVertex = gl_in[1].gl_Position;
	vec4 thirdVertex = gl_in[2].gl_Position;
	vec4 worldPosition = mix(mix(firstVertex, secondVertex, 0.5), thirdVertex,
			0.5);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	vec3 normal = normalize(
			cross(secondVertex.xyz - firstVertex.xyz,
					thirdVertex.xyz - firstVertex.xyz));

	vec3 toCamVector = mix(
			mix(pass_toCameraVector[0], pass_toCameraVector[1], 0.5),
			pass_toCameraVector[2], 0.5);

	vec4 vertex1 = projectionMatrix * viewMatrix * firstVertex;
	vec4 vertex2 = projectionMatrix * viewMatrix * secondVertex;
	vec4 vertex3 = projectionMatrix * viewMatrix * thirdVertex;
	vec4 clipSpaceCoords = mix(mix(vertex1, vertex2, 0.5), vertex3, 0.5);

	vec2 texCoordsGrid = clipSpaceToTexCoords(clipSpaceCoords);

	out_color_reflect = getBlurredColor(reflectionTexture,
			vec2(texCoordsGrid.x, 1.0 - texCoordsGrid.y));
	out_color_refract = getBlurredColor(refractionTexture, texCoordsGrid);
	out_color_skyBox = texture(skyBoxTexture, texCoordsGrid);
	out_frensel = frensel(normal, toCamVector);
	out_waterBottom = texture(depthTexture, texCoordsGrid).r;
	out_visibility = calculateVisibility(positionRelativeToCamera.xyz);

	gl_Position = vertex1;
	EmitVertex();

	gl_Position = vertex2;
	EmitVertex();

	gl_Position = vertex3;
	EmitVertex();

	EndPrimitive();
}
