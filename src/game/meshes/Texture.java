package game.meshes;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public class Texture {

    public enum TextureType {
        FIRE,

        ROCK,
        STONE,
        CRACKS,
        STONE_SHARD,

        MUD,
        GRASS,
        DIRT,
        DIRT_SHARD,

        WOOD,
        WOOD_SHARD,

        RUST,
        METAL,
        METAL_SHARD,

        GLASS,
        GLASS_SHARD,

        // objects
        AXE,

        // other
        HUD,
        CROSSHAIR,
        TEXT,

    }

    private int textureIndex, texture;
    private int width, height;
    private ByteBuffer pixels;

    public Texture(ByteBuffer pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        texture = GL11.glGenTextures();
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureIndex);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

    public int getIndex() {
        return textureIndex;
    }

    public void prepare() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                pixels);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameterfv(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, new float[] { 1, 0, 1, 1 });
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void setTextureIndex(int index) {
        this.textureIndex = index;
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    }
}
