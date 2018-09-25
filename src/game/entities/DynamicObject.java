package game.entities;

import ai.Patrol;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.World;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.particles.ParticleInfo;
import game.graphics.particles.ParticleSystem;
import game.graphics.particles.ParticleType;
import game.meshes.Texture.TextureType;

public class DynamicObject extends Entity {
    protected boolean moved = true;
    protected boolean destroyed;
    private Entity destroyer;
    protected boolean flying;
    protected float speed = 0.1f;
    protected Patrol patrol;
    protected float verticalSpeed;

    // non-living objects that can move

    public DynamicObject(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z) {
        this(prType, texType, shader, x, y, z, 0, 0, 0, 1);
    }

    public DynamicObject(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z, float rx,
            float ry, float rz, float scale) {
        super(prType, texType, shader, x, y, z, rx, ry, rz, scale);

        name = prType.name() + " " + World.dynamicObjects.size();
    }

    public void checkFlying() {
        // temp
        float scale = transform.getScale();
        if (name.startsWith("Player")) {
            scale = Player.actualHeight;
        }
        if (transform.getPosition().y - scale / 2 > landingHeight + 0.01f) {
            flying = true;
        }
    }

    public void destroy(Entity destroyer) {
        this.destroyer = destroyer;
        destroyed = true;

        ParticleSystem.addParticle(ParticleType.WOOD_SHARD, ParticleInfo.EXPLOSION, getPosition());
        // System.out.println(this + " destroyed");
    }

    public Entity getDestroyer() {
        return destroyer;
    }

    public boolean hasMoved() {
        if (moved) {
            moved = false;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void move(Vec3 offset) {
        if (offset.length() == 0) {
            return;
        }

        Vec3 delta = Misc.getDeltaMovement(offset, new Vec3(transform.getRotation()));
        if (flying) {
            delta = delta.mul(1f);
        }

        transform.translate(delta.mul(speed));

        if (offset.y == 0) {
            checkFlying();
        }
        moved = true;
    }

    @Override
    public void onCollision(Entity e) {

    }

    public void rotate(Vec3 rot) {
        if (rot.equals(new Vec3(0, 0, 0))) {
            return;
        }
        float dx = 0, dy;

        if (rot.x < 0f) {
            if (transform.getRotation().x + rot.x / 20f > Misc.MIN_CAMERA_PITCH) {
                dx = rot.x / 20f;
            }
        } else if (rot.x > 0) {
            if (transform.getRotation().x + rot.x / 20 < Misc.MAX_CAMERA_PITCH) {
                dx = rot.x / 20f;
            }
        }

        dy = rot.y / 20f;

        transform.rotate(new Vec3(dx, dy, 0));
        if (Math.abs(transform.getRotation().y) >= 2 * Misc.PI) {
            if (transform.getRotation().y > 0) {
                transform.getRotation().y -= 2 * Misc.PI;
            } else {
                transform.getRotation().y += 2 * Misc.PI;
            }

        }
        moved = true;
    }

    @Override
    public void update() {
        // if (permamanentTranslation != null) {
        // move(permamanentTranslation.mul(5));
        // distance += 0.1f;
        // if (distance >= maxDistance) {
        // distance = 0;
        // permamanentTranslation = null;
        // }
        // }

        if (patrol != null) {
            patrol.update();
        }
        if (transform.isOutSideWorld()) {
            destroy(null);
        }
        super.update();
    }

}
