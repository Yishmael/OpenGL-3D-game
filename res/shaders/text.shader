#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;
layout(location=1) in vec2 tex_coords;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);
uniform mat4 u_mMatrix = mat4(1);

out vec4 v_color;
out vec2 v_tex;

void main() {
	gl_Position = u_pMatrix * u_vMatrix * u_mMatrix * vec4(in_position, 1);
	v_tex = tex_coords;
	v_color = gl_Position; 
}

#shader fragment
#version 420 core

in vec4 v_color;
in vec2 v_tex;

out vec4 out_color;

uniform sampler2D u_texture;
uniform vec4 u_color;

void main() {
	vec4 texel = texture(u_texture, v_tex);
	
	if (texel.r + texel.g + texel.b < 1) {
		discard;
	}
	
	out_color =  texture(u_texture, v_tex).rgba * u_color;
	
	// out_color = vec4(dist*intensity, 0.5);
	// out_color = vec4(1, 0, 0, 0.5);
}