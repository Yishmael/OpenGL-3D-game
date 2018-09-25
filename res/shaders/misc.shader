#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);
uniform mat4 u_mMatrix = mat4(1);

void main() {
	gl_Position = u_pMatrix * u_vMatrix * u_mMatrix * vec4(in_position, 1);
}
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader fragment
#version 420 core

out vec4 out_color;

uniform vec3 u_color;

void main() {
	out_color =  vec4(u_color, 1);
}





