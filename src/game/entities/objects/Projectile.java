package game.entities.objects;

import audio.Audio;
import audio.Sound.SoundType;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.World;
import game.entities.DynamicObject;
import game.entities.Entity;
import game.graphics.particles.ParticleInfo;
import game.graphics.particles.ParticleSystem;
import game.graphics.particles.ParticleType;
import game.meshes.Texture.TextureType;

public class Projectile extends DynamicObject {

    private float speed = 0.1f;
    private Vec3 origin, direction;
    public Entity spawn;
    private String tag;
    private int damage;

    // TODO: object with no physics, but with collision, event triggered when colliding with a
    // physical object

    public Projectile(PrimitiveType type, Vec3 origin, Vec3 direction, float speed, float size) {
        super(type, TextureType.METAL, null, origin.x, origin.y, origin.z, 0, 0, 0, size);
        this.origin = origin;
        this.direction = direction.normalize();
        this.speed = speed;
        name = "Projectile " + World.projectiles.size();

        
        // TODO do this in ProjectileSystem
        World.projectiles.add(this);
    }

    @Override
    public void update() {
        transform.translate(direction.mul(speed));
        super.update();
    }

    public String getTag() {
        return tag;
    }

    public int getDamage() {
        return damage;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onCollision(Entity e) {
        if (destroyed) {
            return;
        }
        if (!tag.equals(e.getName())) {
            destroy(e);
            ParticleSystem.addParticle(ParticleType.FIRE, ParticleInfo.EXPLOSION, getPosition());
            Audio.playSoundAt(SoundType.EXPLOSION, getPosition());
        }
    }

}
