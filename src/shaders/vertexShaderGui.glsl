#version 400

in vec2 position;
out vec2 out_textureCoords;

uniform mat4 transformationMatrix;

void main(void) {
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	out_textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}
