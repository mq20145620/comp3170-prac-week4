#version 410

in vec2 a_position;	

uniform mat4 u_viewMatrix;
uniform mat4 u_worldMatrix;

void main() {
	/* turn 2D point into 4D homogeneous form by appending (0,1) */

	vec4 position = vec4(a_position, 0, 1);
	
	/* apply both the world matrix and the view matrix */
	
	position = u_viewMatrix * u_worldMatrix * position;
	
    gl_Position = position;
}

