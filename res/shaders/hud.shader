#shader vertex
#version 420 core

layout(location=0) in vec2 in_position;
layout(location=1) in vec2 tex_coords;
layout(location=2) in vec4 color;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);
uniform mat4 u_mMatrix = mat4(1);

out vec4 v_color;
out vec2 v_tex;

void main() {
	gl_Position = u_pMatrix * u_mMatrix * vec4(in_position, -1, 1);
	v_tex = tex_coords;
	v_color = color;
}

#shader fragment
#version 420 core

in vec4 v_color;
in vec2 v_tex;

out vec4 out_color;

uniform sampler2D u_texture;

void main() { 
	out_color =  texture(u_texture, v_tex) + v_color;
	
	// out_color = vec4(dist*intensity, 0.5);
	// out_color = vec4(1, 0, 0, 0.5);
}