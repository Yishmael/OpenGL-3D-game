package game.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import engine.utils.Vec3;

public class Shader {
    public enum ShaderType {
        BASIC, SKYBOX, HUD, TERRAIN, TEXT, MISC, PARTICLE, MINIMAL;
    }

    private int program, vs, gs, fs;
    private ShaderType type;
    private HashMap<String, Integer> locations = new HashMap<>();

    public Shader(ShaderType type) {
        this.type = type;
        program = GL20.glCreateProgram();
    }

    public void compile(String[] shaderSrc) {
        vs = compileShader(GL20.GL_VERTEX_SHADER, shaderSrc[0]);
        if (shaderSrc[1] != "") {
            gs = compileShader(GL32.GL_GEOMETRY_SHADER, shaderSrc[1]);
        }
        fs = compileShader(GL20.GL_FRAGMENT_SHADER, shaderSrc[2]);

        GL20.glAttachShader(program, vs);

        if (gs != 0) {
            GL20.glAttachShader(program, gs);
        }
        GL20.glAttachShader(program, fs);

        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);

        loadUniformLocations();
    }

    public void bind() {
        GL20.glUseProgram(program);
    }

    public void clean() {
        GL20.glDetachShader(program, vs);
        // GL20.glDetachShader(program, gs);
        GL20.glDetachShader(program, fs);

        GL20.glDeleteShader(vs);
        // GL20.glDeleteShader(gs);
        GL20.glDeleteShader(fs);

        GL20.glDeleteProgram(program);
    }

    private int compileShader(int type, String src) {
        int s = GL20.glCreateShader(type);
        GL20.glShaderSource(s, src);
        GL20.glCompileShader(s);

        if (GL20.glGetShaderi(s, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println(this.type);
            System.err.println(GL20.glGetShaderInfoLog(s));
        }
        return s;
    }

    private void loadUniformLocations() {
        locations.put("u_pMatrix", GL20.glGetUniformLocation(program, "u_pMatrix"));
        locations.put("u_vMatrix", GL20.glGetUniformLocation(program, "u_vMatrix"));
        locations.put("u_mMatrix", GL20.glGetUniformLocation(program, "u_mMatrix"));
        locations.put("u_texture", GL20.glGetUniformLocation(program, "u_texture"));
        String[] lights = new String[] { "directionalLight", "otherLight" };
        for (String light: lights) {
            locations.put(light + ".light.color", GL20.glGetUniformLocation(program, light + ".light.color"));
            locations.put(light + ".light.intensity", GL20.glGetUniformLocation(program, light + ".light.intensity"));
            locations.put(light + ".direction", GL20.glGetUniformLocation(program, light + ".direction"));
        }
        locations.put("u_color", GL20.glGetUniformLocation(program, "u_color"));
        locations.put("u_alpha", GL20.glGetUniformLocation(program, "u_alpha"));

        // TODO make shader subclasses
        for (Iterator<Map.Entry<String, Integer>> iter = locations.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<String, Integer> entry = iter.next();
            if (entry.getValue() == -1) {
                 iter.remove();
            }
        }
    }

    public void uploadInteger(String name, int texture) {
        GL20.glUniform1i(locations.get(name), texture);
    }

    public void uploadMatrix4f(String name, Matrix4f matrix) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4 * 4);
        matrix.get(fb);
        GL20.glUniformMatrix4fv(locations.get(name), false, fb);
    }

    public void uploadVector3f(String name, Vec3 vec3) {
        GL20.glUniform3f(locations.get(name), vec3.x, vec3.y, vec3.z);
    }

    public void uploadFloat(String name, float value) {
        GL20.glUniform1f(locations.get(name), value);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public ShaderType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Shader: " + type.toString();
    }

    public void uploadVector4f(String name, Vector4f value) {
        GL20.glUniform4f(locations.get(name), value.x, value.y, value.z, value.w);
    }

}
