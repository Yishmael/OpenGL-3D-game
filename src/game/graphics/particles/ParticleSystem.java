package game.graphics.particles;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector4f;

import audio.Audio;
import audio.Sound;
import audio.Source;
import audio.Sound.SoundType;
import engine.utils.Color;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.Misc;
import game.graphics.text.ScreenText;
import game.meshes.Texture.TextureType;

public abstract class ParticleSystem {
    private static ArrayList<Particle> particles = new ArrayList<>();
    protected static int fragmentCount;

    public static void init() {
        ParticleSystem.initVelocities();
    }

    public static void draw() {
        for (Particle p: particles) {
            p.draw();
        }

        ScreenText.drawString("Fragment count:" + fragmentCount, new Vec2(-0.9f, 0.65f), Color.BLACK, 7);
    }

    public static void addImpactParticle(TextureType target, Vec3 position) {
        particles.add(new Particle(Surface.getImpactParticleType(target), ParticleInfo.IMPACT, position));
    }

    public static void addParticle(ParticleType type, ParticleInfo movement, Vec3 position) {
        particles.add(new Particle(type, movement, position));

    }

    public static void update() {
        for (Iterator<Particle> iter = particles.iterator(); iter.hasNext();) {
            Particle p = iter.next();
            p.update();

            if (p.hasEnded()) {
                iter.remove();
            }
        }
    }

    protected static void initVelocities() {
        for (ParticleInfo movement: ParticleInfo.values()) {
            Vec3[] velocities = movement.getFragmentVelocities();

            switch (movement) {
            case EXPLOSION: {
                float speed = 0.1f;
                for (int i = 0; i < velocities.length; i++) {
                    float x = Misc.random(-speed, speed);
                    float y = Misc.random(-speed, speed);
                    float z = Misc.random(-speed, speed);
                    velocities[i] = new Vec3(x, y, z);
                }
            }
                break;
            case IMPACT: {
                float speed = 0.2f;
                for (int i = 0; i < velocities.length; i++) {
                    float x = Misc.random(-speed, speed);
                    float y = Misc.random(-speed * 3 / 2f, speed / 2);
                    float z = Misc.random(-speed, speed);
                    velocities[i] = new Vec3(x, y, z);
                }
            }
                break;
            case FLAME: {
                float speed = 0.014f;
                for (int i = 0; i < velocities.length; i++) {
                    float x = Misc.random(-speed / 7f, speed / 7f);
                    float y = Misc.random(0, speed);
                    float z = Misc.random(-speed / 7f, speed / 7f);
                    velocities[i] = new Vec3(x, y, z);
                }
            }

            }
        }
    }

}
