package game.inventory.weapons;

import org.joml.Math;

import engine.utils.Vec3;

// a cone in front of the unit after performing a melee attack
public class Cone {
    private final float maxAngle = (float) Math.toRadians(30);
    private String tag;
    private Vec3 origin, direction;
    private float maxDistance;
    private boolean disabled;

    public Cone() {
        this(new Vec3(), new Vec3(0, 0, -1), "DEFAULT CONE", 0);
        disabled = true;
    }

    public Cone(Vec3 origin, Vec3 direction, String tag, float maxDistance) {
        this.origin = new Vec3(origin);
        this.direction = new Vec3(direction);
        this.tag = tag;
        this.maxDistance = maxDistance;
    }

    public float getMaxAngle() {
        return maxAngle;
    }

    public Vec3 getPosition() {
        return origin;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public String getTag() {
        return tag;
    }

    public void disable() {
        disabled = true;
    }

    public boolean reaches(Vec3 position) {
        if (disabled) {
            return false;
        }
        if (origin.distanceTo(position) > maxDistance) {
            return false;
        }

        float angle = (float) Math.acos(position.sub(origin).normalize().dot(direction));

        disable();

        return angle < Math.toRadians(30);
    }
}
