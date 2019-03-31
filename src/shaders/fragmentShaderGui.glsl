#version 400

in vec2 out_textureCoords;
out vec4 out_Color;

uniform sampler2D tex;
uniform vec4 blend_color;
uniform float opacity;

void main(void) {
	out_Color = texture(tex, out_textureCoords);
	out_Color = vec4(out_Color * blend_color);
	out_Color.a = out_Color.a * opacity;
}
