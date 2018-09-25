#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;
layout(location=1) in vec2 in_uv;
layout(location=2) in vec3 in_normal;

out vec2 tex_uv;
out vec3 normal;

uniform mat4 u_pMatrix = mat4(1);
uniform mat4 u_vMatrix = mat4(1);
uniform mat4 u_mMatrix = mat4(1);

void main() {
	gl_Position = u_pMatrix * u_vMatrix * u_mMatrix * vec4(in_position, 1);
	// gl_Position = u_transMatrix * u_rotMatrix * u_scaleMatrix * vec4(in_position, 1);
	// tex_uv = (u_mMatrix * vec4(in_uv, 0, 1));
	tex_uv = in_uv;
	normal = -normalize((u_mMatrix * vec4(in_normal, 0)).xyz);
}
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader fragment
#version 420 core
in vec2 tex_uv;
in vec3 normal;
in vec4 o;
out vec4 out_color;

uniform sampler2D u_texture;

struct Light {
	vec3 color;
	float intensity;
};

struct DirectionalLight {
	Light light;
	vec3 direction;
};

uniform DirectionalLight directionalLight = 
		DirectionalLight(Light(vec3(1, 1, 1), 1), normalize(vec3(0, 1000, -1000)));
uniform DirectionalLight otherLight;
uniform float u_alpha = 1;
		
vec3 getTotalIntensity() {
	float ambient = 0.3;
	vec3 intensity = vec3(0);
	vec3 total = vec3(0) + ambient;

	intensity = directionalLight.light.color * directionalLight.light.intensity;
	total += intensity * max(dot(normal, -directionalLight.direction), ambient);
	
	intensity = otherLight.light.color * otherLight.light.intensity;
	total += intensity * max(dot(normal, -otherLight.direction), 0);
	
	total = min(total, vec3(1));
	
	return total;
}

void main() {
	vec4 texel = texture(u_texture, tex_uv);
	
	vec3 totalIntensity = getTotalIntensity();
	
	out_color =  vec4(texel.rgb * totalIntensity, texel.a * u_alpha);
	// out_color =  vec4(vec3(1) * totalIntensity, texel.a);
	// out_color =  vec4(normal, 1);
}





