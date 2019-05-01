#version 400 core

in vec2 position;
in vec4 indicators;

out vec4 pass_clipSpaceReal;
out vec4 pass_clipSpaceGrid;
out vec3 pass_toCameraVector;
out vec4 pass_positionRelativeToCamera;
flat out vec3 pass_normal;
out float visibility;
out vec4 pass_specular;
out vec4 pass_diffuse;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPos;
uniform float waveTime;
uniform vec2 gridCoords;
uniform float density;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec2 lightBias;

const float gradient = 15;
const float PI = 3.1415926535897932384626433832795;
const float waveLength = 0.4;
const float waveAmplitude = 2;
const float SIZE = 800;
const float specularReflectivity = 0.4;
const float shineDamper = 20.0;

float generateOffset(float x, float z, float val1, float val2) {
	float radiansX = ((mod(x+z*x*val1, waveLength)/waveLength) + waveTime * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z*x +x*z), waveLength)/waveLength) + waveTime * 2.0 * mod(x , 2.0) ) * 2.0 * PI;
	return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

vec3 applyDistortion(vec3 vertex) {
	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
	float zDistortion = generateOffset(vertex.x, vertex.z , 0.15, 0.2);
	return vec3(vertex.x + xDistortion, yDistortion, vertex.z + zDistortion);
}

vec3 calculateNormal(vec3 vertex0, vec3 vertex1, vec3 vertex2) {
	vec3 u = vertex1 - vertex0;
	vec3 v = vertex2 - vertex0;
	return normalize(cross(u, v));
}

vec4 calculateSpecularLighting(vec3 toCamVector, vec3 toLightVector, vec3 normal) {
	vec3 reflectedLightDirection = reflect(-toLightVector, normal);
	float specularFactor = dot(reflectedLightDirection , toCamVector);
	specularFactor = max(specularFactor,0.0);
	specularFactor = pow(specularFactor, shineDamper);
	return vec4(specularFactor * specularReflectivity * lightColor, 1.0);
}

vec4 calculateDiffuseLighting(vec3 toLightVector, vec3 normal){
	float brightness = max(dot(toLightVector, normal), 0.0);
	return vec4((lightColor * lightBias.x) + (brightness * lightColor * lightBias.y), 1.0);
}

void main(void) {
	vec3 currentPosition = vec3(position.x + (gridCoords.x * SIZE), 0, position.y + (gridCoords.y * SIZE));
	vec3 vertex1 = currentPosition + vec3(indicators.x, 0., indicators.y);
	vec3 vertex2 = currentPosition + vec3(indicators.z, 0., indicators.w);

	pass_positionRelativeToCamera = viewMatrix * transformationMatrix * vec4(currentPosition, 1.0);
	pass_clipSpaceGrid =  projectionMatrix * pass_positionRelativeToCamera;

	currentPosition = applyDistortion(currentPosition);
	vertex1 = applyDistortion(vertex1);
	vertex2 = applyDistortion(vertex2);

	pass_normal = calculateNormal(currentPosition, vertex1, vertex2);

	pass_toCameraVector = normalize(cameraPos - currentPosition);

	pass_clipSpaceReal = projectionMatrix * viewMatrix * transformationMatrix * vec4(currentPosition, 1.0);

	gl_Position = pass_clipSpaceReal;

	vec3 lightDirection = gl_Position.xyz - lightPosition;
	vec3 toLightVector = normalize(lightDirection);
	pass_specular = calculateSpecularLighting(pass_toCameraVector, toLightVector, pass_normal);
	pass_diffuse = calculateDiffuseLighting(toLightVector, pass_normal);
}
