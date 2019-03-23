#version 400 core

in vec2 position;
in vec2 textureCoords;

out vec4 pass_clipSpaceReal;
out vec4 pass_clipSpaceGrid;
out vec3 pass_toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform vec3 cameraPos;
uniform float waveTime;
uniform vec2 gridCoords;
uniform float density;

const float gradient = 15;
const float PI = 3.1415926535897932384626433832795;
const float waveLength = 0.4;
const float waveAmplitude = 0.7;
const float SIZE = 800;

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

void main(void) {
	vec3 currentPosition = vec3(position.x + (gridCoords.x * SIZE), 0, position.y + (gridCoords.y * SIZE));

	currentPosition = applyDistortion(currentPosition);

	pass_clipSpaceGrid = transformationMatrix * vec4(currentPosition, 1.0);
	pass_toCameraVector = normalize(cameraPos - currentPosition);

//	pass_clipSpaceReal = transformationMatrix * vec4(currentPosition, 1.0);

	gl_Position = pass_clipSpaceGrid;
}
