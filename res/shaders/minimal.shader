#shader vertex
#version 420 core

layout(location=0) in vec3 in_position;

out vec3 vPosition;

void main() {
	gl_Position = vec4(in_position, 1);
	
	vPosition = in_position;
}

#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader geometry
#version 420 core

layout(points) in;
layout(line_strip, max_vertices=2) out;

in vec3 vPosition[];

void main(){
	gl_Position = gl_in[0].gl_Position + vec4(-0.2, 0, 0, 0);
	EmitVertex();
	
	gl_Position = gl_in[0].gl_Position + vec4(0, 0.2, 0, 0);
	EmitVertex();
	
	// gl_Position = gl_in[2].gl_Position + vec4(0.2, 0, 0, 0);
	// EmitVertex();
	
	EndPrimitive();
	
}
#>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
#shader fragment
#version 420 core

in vec4 v_color;

out vec4 out_color;

void main() {

	// out_color =  v_color;
	out_color = vec4(1, 0, 0, 1);
}