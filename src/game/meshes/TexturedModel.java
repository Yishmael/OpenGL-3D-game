package game.meshes;

import engine.GameObject.PrimitiveType;
import game.data.Resources;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.graphics.lighting.DirectionalLight;
import game.graphics.materials.BasicMaterial;
import game.graphics.materials.Material;
import game.meshes.Texture.TextureType;
import game.utils.Loader;

public class TexturedModel {
    private Mesh mesh;
    private Texture texture;
    private TextureType texType;

    public TexturedModel(PrimitiveType prType, TextureType texType, Shader shader) {
        this.texType = texType;
        mesh = Loader.loadMesh(prType);
        mesh.setShader(Resources.shaders.get(ShaderType.BASIC));

        Material material = new BasicMaterial(mesh.getShader(), new DirectionalLight());
        material.setTransparency(0);
        material.applyMaterial();

        texture =   Loader.loadTexture(texType);

    }

    public Mesh getMesh() {
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }
    public TextureType getTextureType() {
        return texType;
    }

    public void bind() {
        mesh.bind();
        texture.bind();
    }

    public void unbind() {
        texture.unbind();
        mesh.unbind();
    }

}
