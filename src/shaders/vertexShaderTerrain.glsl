#version 400 core

in vec3 position;
in vec3 color;
in vec3 normal;

flat out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
flat out vec3 pass_color;
out float visibility;
out vec4 clipSpaceCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;
uniform vec3 lightColor;

uniform float density;
const float gradient = 15;

uniform vec4 plane;


void main(void) {

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, plane);

	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	clipSpaceCoords = gl_Position;

	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	pass_color = color;

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

}
