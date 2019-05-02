#version 400 core

in vec2 position;
in vec4 indicators;

out vec4 pass_clipSpaceReal;
out vec4 pass_clipSpaceGrid;
out vec3 pass_toCameraVector;
out vec4 pass_positionRelativeToCamera;
flat out vec3 pass_normal;
out float visibility;
flat out vec4 pass_specular;
flat out vec4 pass_diffuse;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPos;
uniform float waveTime;
uniform vec2 gridCoords;
uniform float density;
uniform int vertexCount;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec2 lightBias;

const float gradient = 15;
const float PI = 3.1415926535897932384626433832795;
const float waveLength = 0.4;
const float waveAmplitude = 2;
const float SIZE = 800;
const float specularReflectivity = 0.4;
const float shineDamper = 20.0;

vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec2 mod289(vec2 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec3 permute(vec3 x) { return mod289(((x*34.0)+1.0)*x); }

float map(float value, float min1, float max1, float min2, float max2) {
  return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

float snoise(vec2 v) {
    const vec4 C = vec4(0.211324865405187,  // (3.0-sqrt(3.0))/6.0
                        0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)
                        -0.577350269189626,  // -1.0 + 2.0 * C.x
                        0.024390243902439); // 1.0 / 41.0
    vec2 i  = floor(v + dot(v, C.yy) );
    vec2 x0 = v -   i + dot(i, C.xx);
    vec2 i1;
    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 x12 = x0.xyxy + C.xxzz;
    x12.xy -= i1;
    i = mod289(i); // Avoid truncation effects in permutation
    vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
        + i.x + vec3(0.0, i1.x, 1.0 ));

    vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);
    m = m*m ;
    m = m*m ;
    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;
    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );
    vec3 g;
    g.x  = a0.x  * x0.x  + h.x  * x0.y;
    g.yz = a0.yz * x12.xz + h.yz * x12.yw;
    return 130.0 * dot(m, g);
}

float generateOffset(float x, float z, float val1, float val2) {
	float radiansX = ((mod(x+z*x*val1, waveLength)/waveLength) + waveTime * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z*x +x*z), waveLength)/waveLength) + waveTime * 2.0 * mod(x , 2.0) ) * 2.0 * PI;
	return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

float generateOffset2(vec2 pos) {
	float a = 0.0;
    float DF = 0.0;

	vec2 vel = vec2(waveTime*.1);
	DF += snoise(pos+vel)*.25+.25;

	// Add a random position
	a = snoise(pos*vec2(cos(waveTime*.15),sin(waveTime*.1)))*3.1415;
	vel = vec2(cos(a),sin(a));
	DF += snoise(pos+vel)*.25+.25;
	return fract(DF);
}

vec3 applyDistortion(vec3 vertex) {
	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
	yDistortion = map(generateOffset2((vertex.xz/SIZE)*10), 0, 1, -1, 1)*waveAmplitude;
	float zDistortion = generateOffset(vertex.x, vertex.z , 0.15, 0.2);
	return vec3(vertex.x + xDistortion, yDistortion, vertex.z + zDistortion);
}

vec3 calculateNormal(vec3 vertex0, vec3 vertex1, vec3 vertex2) {
	vec3 u = vertex1 - vertex0;
	vec3 v = vertex2 - vertex0;
	return normalize(cross(u, v));
}

vec4 calculateSpecularLighting(vec3 toCamVector, vec3 toLightVector, vec3 normal) {
	vec3 reflectedLightDirection = reflect(-toLightVector, normal);
	float specularFactor = dot(reflectedLightDirection , toCamVector);
	specularFactor = max(specularFactor,0.0);
	specularFactor = pow(specularFactor, shineDamper);
	return vec4(specularFactor * specularReflectivity * lightColor, 1.0);
}

vec4 calculateDiffuseLighting(vec3 toLightVector, vec3 normal){
	float brightness = max(dot(toLightVector, normal), 0.0);
	return vec4((lightColor * lightBias.x) + (brightness * lightColor * lightBias.y), 1.0);
}

void main(void) {
	vec3 currentPosition = vec3(position.x + (gridCoords.x * SIZE), 0, position.y + (gridCoords.y * SIZE));
	vec3 vertex1 = currentPosition + vec3(indicators.x, 0., indicators.y);
	vec3 vertex2 = currentPosition + vec3(indicators.z, 0., indicators.w);

	pass_positionRelativeToCamera = viewMatrix * transformationMatrix * vec4(currentPosition, 1.0);
	pass_clipSpaceGrid =  projectionMatrix * pass_positionRelativeToCamera;

	currentPosition = applyDistortion(currentPosition);
	vertex1 = applyDistortion(vertex1);
	vertex2 = applyDistortion(vertex2);

	pass_normal = calculateNormal(currentPosition, vertex1, vertex2);

	pass_toCameraVector = normalize(cameraPos - currentPosition);

	pass_clipSpaceReal = projectionMatrix * viewMatrix * transformationMatrix * vec4(currentPosition, 1.0);

	gl_Position = pass_clipSpaceReal;

	vec3 lightDirection = gl_Position.xyz - lightPosition;
	vec3 toLightVector = -normalize(lightDirection);
	pass_specular = calculateSpecularLighting(pass_toCameraVector, toLightVector, pass_normal);
	pass_diffuse = calculateDiffuseLighting(toLightVector, pass_normal);

}
