#version 400 core

in vec3 position;
out vec3 pass_position;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform float hourOfDay;

void main(void) {
	vec4 worldPos = vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * worldPos;


	pass_position = position;
}
