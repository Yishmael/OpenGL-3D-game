package game.graphics;

import game.CoreCamera;
import game.data.Resources;
import game.graphics.Shader.ShaderType;
import game.utils.Loader;

public abstract class ShaderSystem {
    public static void init() {
        
        for (ShaderType shader: ShaderType.values()) {
            Loader.loadShader(shader);
        }
        
        Shader shader = Resources.shaders.get(ShaderType.BASIC);
        shader.bind();
        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
    }
}
