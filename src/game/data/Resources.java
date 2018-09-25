package game.data;

import java.util.HashMap;

import audio.Sound;
import audio.Sound.SoundType;
import engine.GameObject.PrimitiveType;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.meshes.Mesh;
import game.meshes.Texture;
import game.meshes.Texture.TextureType;

public abstract class Resources {

    public static HashMap<PrimitiveType, Mesh> meshes = new HashMap<>();
    public static HashMap<TextureType, Texture> textures = new HashMap<>();
    public static HashMap<ShaderType, Shader> shaders = new HashMap<>();
    public static HashMap<SoundType, Sound> sounds = new HashMap<>();
    public static HashMap<Integer, float[]> stringPositions = new HashMap<>();
//    public static HashMap<ParticleType, Particle> particles = new HashMap<>();
}
