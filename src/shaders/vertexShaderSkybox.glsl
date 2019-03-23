#version 400

in vec3 position;
out vec3 textureCoords;
out vec3 pass_color;
out float pass_height;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 skyTopColor;
uniform vec3 skyBottomColor;
uniform vec4 plane;
uniform float SIZE;

float map(float value, float min1, float max1, float min2, float max2) {
  return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main(void){
	vec4 worldPos = vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPos;
	gl_ClipDistance[0] = dot(worldPos, plane);
	textureCoords = position;
	float height = 1;
	if(position.y < 0) height = 0;
	pass_height = height;
}
