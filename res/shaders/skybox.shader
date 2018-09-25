#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;

out vec3 texCoords;

uniform mat4 u_pMatrix;
uniform mat4 u_vMatrix;

void main() {
	gl_Position = u_pMatrix * u_vMatrix * vec4(in_position, 1);
	texCoords = in_position;
}

#shader fragment
#version 420 core

in vec3 texCoords;

out vec4 out_color;

uniform samplerCube u_texture;

void main() {
	out_color = texture(u_texture, texCoords);
}