#version 400 core

in vec3 pass_position;
out vec4 out_Color;

void main(void) {
	out_Color = vec4(1, 1, 0, 1-length(pass_position));
}
