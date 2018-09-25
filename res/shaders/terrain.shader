#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;
layout(location=1) in vec2 in_uv;
layout(location=2) in vec3 in_normal;

out vec2 tex_uv;
out vec3 normal;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);

void main() {
	gl_Position = u_pMatrix * u_vMatrix * vec4(in_position, 1);
	// gl_Position = u_transMatrix * u_rotMatrix * u_scaleMatrix * vec4(in_position, 1);
	tex_uv = in_uv;
	normal = normalize(in_normal);
}

#shader fragment
#version 420 core

in vec2 tex_uv;
in vec3 normal;

out vec4 out_color;

uniform sampler2D u_texture;

void main() {
	vec3 ambientLightIntensity = vec3(0.4, 0.4, 0.4);
	vec3 sunIntensity = vec3(1, 1, 1);
	vec3 sunDirection = -normalize(vec3(0, 1000, -1000));

	vec4 texel = texture(u_texture, tex_uv / 20);
	
	float n = max(dot(-normal, sunDirection), 0);
	
	vec3 totalIntensity = ambientLightIntensity + sunIntensity * max(dot(-normal, sunDirection), 0);
	// out_color = vec4(n, n, n, 1);
	// out_color = vec4(normal.xyz, 1);
	out_color = vec4(texel.rgb * totalIntensity, texel.a);
}