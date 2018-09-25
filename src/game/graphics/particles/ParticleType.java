package game.graphics.particles;

import game.meshes.Texture.TextureType;

public enum ParticleType {

    METAL_SHARD(new TextureType[] { TextureType.METAL, TextureType.RUST }, 1),
    GLASS_SHARD(new TextureType[] { TextureType.GLASS }, 1),
    WOOD_SHARD(new TextureType[] { TextureType.WOOD }, 1),
    DIRT_SHARD(new TextureType[] { TextureType.DIRT, TextureType.MUD, TextureType.GRASS }, 1),
    STONE_SHARD(new TextureType[] { TextureType.STONE, TextureType.ROCK, TextureType.CRACKS }, 1),
    FIRE(new TextureType[] { TextureType.FIRE }, 2);

    private TextureType[] sourceTextures;
    private float scale;

    ParticleType(TextureType[] sourceTextures, float scale) {
        this.sourceTextures = sourceTextures;
        this.scale = scale;
    }

    public TextureType[] getSourceTextures() {
        return sourceTextures;
    }

    public float getScale() {
        // temp
        return scale * 2;
    }
}
