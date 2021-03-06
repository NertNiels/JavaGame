#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 color;

out vec2 pass_textureCoords;
flat out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
flat out vec3 pass_color;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 plane;
uniform vec3 lightPosition;
uniform float distortionTime;
uniform float distortionFactor;

uniform float density;
const float gradient = 15;

vec4 applyDistortion(vec4 position, float heightFactor) {
	vec4 distorted = vec4(position);

	distorted.x += heightFactor * sin(distortionTime + position.x)
			* cos(distortionTime - position.z) * sin(distortionTime - 1003)
			* distortionFactor * 0.1;
	distorted.y += heightFactor * sin(distortionTime + position.y)
				* cos(distortionTime - position.y) * sin(distortionTime + 1241)
				* distortionFactor * 0.01;
	distorted.z += heightFactor * sin(distortionTime + position.z)
					* cos(distortionTime - position.x) * sin(distortionTime - 124)
					* distortionFactor * 0.1;

	return distorted;
}


void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	worldPosition = applyDistortion(worldPosition, max(position.y, 0));
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	gl_ClipDistance[0] = dot(worldPosition, plane);
	pass_textureCoords = textureCoords;
	pass_color = color;

	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz
			- worldPosition.xyz;

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

}
