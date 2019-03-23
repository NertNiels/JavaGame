#version 400

in vec3 textureCoords;
in vec3 pass_color;
in float pass_height;
out vec4 out_Color;

uniform vec3 skyTopColor;
uniform vec3 skyBottomColor;


float smoothlystep(float edge0, float edge1, float x) {
  x = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
  return x * x * (3 - 2 * x);
}

void main(void){
    out_Color = vec4(mix(skyBottomColor, skyTopColor, smoothlystep(0.5, 0.55, pass_height)), 1.0);
}
