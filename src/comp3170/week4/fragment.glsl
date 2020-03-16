#version 410

in vec2 v_position;

uniform vec3 u_innerColour;
uniform vec3 u_outerColour;

layout(location = 0) out vec4 colour;

void main() {
	float l = length(v_position);
    vec3 c = mix(u_innerColour, u_outerColour, l);
    colour = vec4(c,1);
}

