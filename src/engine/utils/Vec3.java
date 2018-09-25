package engine.utils;

import org.joml.Vector3f;

public class Vec3 {
    public float x, y, z;

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3(float x) {
        this(x, x, x);
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vec3(Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    // TEMP
    public Vec3(Vector3f vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3 add(Vec3 rhs) {
        return new Vec3(x + rhs.x, y + rhs.y, z + rhs.z);
    }

    public Vec3 cross(Vec3 rhs) {
        float x1 = x;
        float y1 = y;
        float z1 = z;

        float x2 = rhs.x;
        float y2 = rhs.y;
        float z2 = rhs.z;

        return new Vec3(y1 * z2 - y2 * z1, x2 * z1 - x1 * z2, x1 * y2 - x2 * y1);
    }

    // aboid sqrt
    public float distanceTo(Vec3 other) {
        float xx = (x - other.x) * (x - other.x);
        float yy = (y - other.y) * (y - other.y);
        float zz = (z - other.z) * (z - other.z);

        return (float) Math.sqrt(xx + yy + zz);
    }

    public float dot(Vec3 rhs) {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    // avoid sqrt
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vec3 mul(float value) {
        return new Vec3(x * value, y * value, z * value);
    }

    public Vec3 negate() {
        x *= -1;
        y *= -1;
        z *= -1;

        return this;
    }

    public Vec3 negated() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 normalize() {
        x /= length();
        y /= length();
        z /= length();

        return this;
    }

    public Vec3 normalized() {
        return new Vec3(x / length(), y / length(), z / length());
    }

    public Vec3 sub(Vec3 rhs) {
        return add(rhs.negated());
    }

    // temp so it's compatible with current Transform
    public Vector3f toVector3f() {
        return new Vector3f(x, y, z);
    }

    public void reset() {
        x = y = z = 0;
    }

    @Override
    public String toString() {
        return "" + x + " " + y + " " + z;
    }
}
