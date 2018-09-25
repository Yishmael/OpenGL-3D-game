package game.utils;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import engine.utils.Vec3;

public class Transform {
    public static final Transform origin = new Transform();

    private Vec3 position, rotation;
    private float scale;

    public Transform(Vec3 position, Vec3 rotation, float scale) {
        // WARNING: KEEPS THE REFERENCE
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform(Transform transform) {
        this.position = new Vec3(transform.getPosition());
        this.rotation = new Vec3(transform.getRotation());
        this.scale = transform.getScale();
    }

    public Transform() {
        this.position = new Vec3();
        this.rotation = new Vec3();
        this.scale = 1;
    }

    public Matrix4f getMatrix() {
        Matrix4f r = new Matrix4f().translation(position.toVector3f()).rotateXYZ(rotation.toVector3f()).scale(scale);
        return r;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void translate(Vec3 translation) {
        this.position.x += translation.x;
        this.position.y += translation.y;
        this.position.z += translation.z;
    }

    public void rotate(Vec3 rotation) {
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
    }

    public void rotateX(float dx) {

        this.rotation.x += dx;
    }

    public void rotateY(float dy) {

        this.rotation.y += dy;
    }

    public void rotateZ(float dz) {

        this.rotation.z += dz;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public void setZ(float z) {
        position.z = z;
    }

    public void setPosition(Vec3 position) {
        this.position.set(position);
    }

    public void setRotation(Vec3 rotation) {
        this.rotation.set(rotation);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void set(Transform transform) {
        this.position.set(transform.getPosition());
        this.rotation.set(transform.getRotation());
        this.scale = transform.getScale();
    }

    public Vec3 getForwardVector() {
        // return Misc.getForwardVector(transform.getRotation());
        Vector4f f = getMatrix().getColumn(2, new Vector4f());
        return new Vec3(f.x, f.y, f.z);
    }

    public boolean isOutSideWorld() {
        int radius = 300;
        return Math.abs(position.x) > radius || Math.abs(position.y) > radius || Math.abs(position.z) > radius;
    }

}
