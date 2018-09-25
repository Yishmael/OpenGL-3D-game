package engine.utils;

import javax.print.attribute.standard.MediaSize.Other;

import org.joml.Vector2f;

public class Vec2 {
    public float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        this(0, 0);
    }

    public Vec2(Vec2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    // avoid sqrt
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vec2 negated() {
        return new Vec2(-x, -y);
    }

    public Vec2 negate() {
        x *= -1;
        y *= -1;

        return this;
    }

    public Vec2 normalize() {
        x /= length();
        y /= length();

        return this;
    }

    public Vec2 normalized() {
        return new Vec2(x / length(), y / length());
    }

    public Vec2 add(Vec2 rhs) {
        return new Vec2(x + rhs.x, y + rhs.y);
    }

    public Vec2 sub(Vec2 rhs) {
        return add(rhs.negated());
    }

    public Vec2 mul(float value) {
        return new Vec2(x * value, y * value);
    }

    public float dot(Vec2 rhs) {
        return x * rhs.x + y * rhs.y;
    }

    // temp so it's compatible with current Transform
    public Vector2f toVector2f() {
        return new Vector2f(x, y);
    }

}
