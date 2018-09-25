#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;
layout(location=1) in vec2 in_uv;

out vec4 vPosition;

void main() {
	gl_Position = vec4(in_position, 1);
	vPosition = gl_Position;
}
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader geometry
#version 420 core

layout(points) in;
layout(triangle_strip, max_vertices=14) out;

in vec4 vPosition[];
out vec3 vColor;
out vec2 vTexCoords;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);
uniform mat4 u_mMatrix = mat4(1);

mat4 transform = u_pMatrix * u_vMatrix * u_mMatrix;

vec3[] positions = {
	vec3(-1, -1, -1),
	vec3( 1, -1, -1),
	vec3(-1,  1, -1),
	vec3( 1,  1, -1),
	vec3(-1, -1,  1),
	vec3( 1, -1,  1),
	vec3(-1,  1,  1),
	vec3( 1,  1,  1),
};

int[] indices = {
	7, 6, 3, 2, 0, 6, 4,
	7, 5, 3, 1, 0, 5, 4,
};

void createCubeAroundPoint(vec4 point, float size) {
	for(int i = 0; i < indices.length; i++) {
		gl_Position = transform * (point + vec4(positions[indices[i]]*size, 1));
	
		if (i % 4 == 0) {
			vTexCoords = vec2(0, 1);
		} else if (i % 4 == 1) {
			vTexCoords = vec2(1, 1);
		} else if (i % 4 == 2) {
			vTexCoords = vec2(0, 0);
		} else if (i % 4 == 3) {
			vTexCoords = vec2(1, 0);
		}
		EmitVertex();
	}
}

void main(){
	vColor = gl_in[0].gl_Position.xyz + vec3(0.3);
	
	createCubeAroundPoint(gl_in[0].gl_Position, 0.03);
}
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader fragment
#version 420 core

in vec2 vTexCoords;
in vec3 vColor;
out vec4 out_color;

uniform sampler2D u_texture;
uniform vec4 u_color = vec4(1);

void main() {
	vec4 texel = texture(u_texture, vTexCoords);
	
	out_color =  vec4(texel.rgba * u_color);
	// out_color =  vec4(vec3(1) * totalIntensity, texel.a);
	// out_color = vec4(vColor, 1);
	// out_color = vec4(1, 0, 0, 1);
}





