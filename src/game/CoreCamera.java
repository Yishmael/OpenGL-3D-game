package game;

import org.joml.Matrix4f;

import engine.utils.Vec3;
import game.entities.units.Player;
import game.utils.Transform;

public class CoreCamera {
    public static final int FOV_MIN = 30, FOV_MAX = 150;
    private static int fov = 90;
    public static float eyeHeightFromCenter = 0.5f;
    private static float zNear = 0.01f, zFar = 1000f;
    private static Matrix4f projMatrix = Misc.createPerspectiveMatrix(fov, zNear, zFar);
    public static CoreCamera instance;
    private Transform transform;
    private boolean hasRotated = true;
    
    
    // TODO make camera a singleton
    private CoreCamera() {
        this(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
    }

    private CoreCamera(Vec3 translation, Vec3 rotation) {
    }

    public static CoreCamera createInstance() {
        return createInstance(new Vec3(), new Vec3());
    }

    public static CoreCamera createInstance(Vec3 translation, Vec3 rotation) {
        if (instance == null) {
            instance = new CoreCamera(translation, rotation);
            instance.transform = new Transform(new Vec3(translation), new Vec3(rotation), 1);
            projMatrix = Misc.createPerspectiveMatrix(fov, zNear, zFar);
            return instance;
        } else {
            return instance;
        }

    }

    public static void fovDelta(int fovDelta) {
        fov = Misc.getClamped(fov + fovDelta, FOV_MIN, FOV_MAX);
        projMatrix = Misc.createPerspectiveMatrix(fov, zNear, zFar);

        // TODO update all proj matrices used by other classes
    }

    public static int getFov() {
        return fov;
    }

    public static Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public static void setFov(int value) {
        fovDelta(value - fov);
    }

    public Vec3 getRotation() {
        hasRotated = true;
        return transform.getRotation();
    }

    public Transform getTransform() {
        hasRotated = true;
        return transform;
    }

    public boolean hasRotated() {
        if (hasRotated) {
            hasRotated = false;
            return true;
        } else {
            return false;
        }
    }

    public void setPosition(Vec3 position) {
        eyeHeightFromCenter = Player.actualHeight / 2 - 0.1f;
        transform.setPosition(position.add(new Vec3(0, eyeHeightFromCenter, 0)));
    }

    public void zoom(int value) {
        fovDelta(-value);
    }

}
