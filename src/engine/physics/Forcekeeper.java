package engine.physics;

import engine.utils.Vec3;

public class Forcekeeper {

    private Vec3 direction = new Vec3();
    private float magnitude;

    public void addForce(Vec3 direction, float magnitude) {
        this.direction.add(direction);
        this.direction.mul(this.magnitude + magnitude);

        this.magnitude = this.direction.length();
        this.direction.normalize();
    }

    public Vec3 getForce() {
        return new Vec3(direction).mul(magnitude);
    }

    public void clear() {
        direction.reset();
        magnitude = 0;
    }
}
