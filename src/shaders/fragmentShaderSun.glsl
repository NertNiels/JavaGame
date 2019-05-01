#version 400 core

flat in vec3 pass_position;
out vec4 out_Color;

uniform vec3 sunColor;

void main(void) {
	out_Color = vec4(sunColor, 1.-length(pass_position.xz/100.));
}
