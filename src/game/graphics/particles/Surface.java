package game.graphics.particles;

import audio.Audio;
import audio.Sound;
import audio.Sound.SoundType;
import audio.Source;
import game.meshes.Texture.TextureType;

public abstract class Surface {

    public static ParticleType getImpactParticleType(TextureType texture) {
        for (ParticleType p: ParticleType.values()) {
            for (TextureType t: p.getSourceTextures()) {
                if (t == texture) {
                    return p;
                }

            }
        }

        System.err.println("Unknown particle texture: " + texture);
        return null;

    }

    public static SoundType getImpactSound(TextureType texture) {
        return SoundType.IMPACT;
    }
}
