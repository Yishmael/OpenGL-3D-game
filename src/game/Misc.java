package game;

import org.joml.Matrix4f;

import engine.utils.Vec3;
import game.graphics.Window;
import game.utils.Transform;

public class Misc {

    public static final float PI = (float) Math.PI;
    public static final float HEAD_HEIGHT = 1;
    public static final float BOTTOM_ELEVATION = 0;
    public static final float MIN_CAMERA_PITCH = (float) Math.toRadians(-87f); // up
    public static final float MAX_CAMERA_PITCH = (float) Math.toRadians(87f); // down
    public static final int MAX_TEXT_LENGTH = 20;
    public static final boolean EXPORTING = false;
    public static final float GRAVITY = 20;
    public static final float JUMP_POWER = 6;

    // temp
    public static final float dt = 1f / 60;

    public static float atan(float x) {
        return (float) Math.atan(x);
    }

    public static float cos(float x) {
        return (float) Math.cos(x);
    }

    public static Matrix4f createOrthographicMatrix() {
        return new Matrix4f().ortho(-1, 1, -1, 1, -1, 1);
    }

    public static Matrix4f createPerspectiveMatrix(float fov, float zNear, float zFar) {
        // return new Matrix4f().ortho(0, 4, 0, 3, -10f, 10f);
        return new Matrix4f().perspective((float) Math.toRadians(fov), Window.instance.getRatio(), zNear, zFar);
    }

    public static Matrix4f createSkyboxViewMatrix(Vec3 rotation) {
        return new Matrix4f().rotateXYZ(rotation.toVector3f());
        // Matrix4f matrix = new Matrix4f();
        // matrix.rotateX(rotation.x);
        // matrix.rotateY(rotation.y);
        // matrix.rotateZ(rotation.z);
        //
        // return matrix;

    }

    public static Matrix4f createTransformationMatrix(Vec3 translation, Vec3 rot, float scale) {
        return new Matrix4f().translation(translation.toVector3f()).rotateXYZ(rot.toVector3f()).scale(scale);
        // Matrix4f matrix = new Matrix4f();
        // matrix.translate(translation);
        // matrix.rotateX(rot.x);
        // matrix.rotateY(rot.y);
        // matrix.rotateZ(rot.z);
        // matrix.scale(new Vec3(scale).mul(1));

        // return matrix;
    }

    public static Matrix4f createViewMatrix(CoreCamera camera) {
        Vec3 translation = camera.getTransform().getPosition();
        Vec3 rotation = camera.getRotation();
        // right handed system
        Vec3 negativeTranslation = translation.negated();

        return new Matrix4f().rotateXYZ(rotation.toVector3f()).translate(negativeTranslation.toVector3f());

        // Matrix4f matrix = new Matrix4f();
        // matrix.rotateX(rotation.x);
        // matrix.rotateY(rotation.y);
        // matrix.rotateZ(rotation.z);
        // matrix.translate(negativeTranslation);
        //
        // return matrix;
    }

    public static Matrix4f createViewMatrix(Transform transform) {
        // temp
        return createViewMatrix(transform.getPosition(), transform.getRotation());
    }

    public static Matrix4f createViewMatrix(Vec3 position, Vec3 rotation) {
        // temp
        Vec3 negativeTranslation = position.negated();
        return new Matrix4f().rotateXYZ(rotation.toVector3f()).translate(negativeTranslation.toVector3f());
    }

    public static int digitCount(int value) {
        return String.valueOf(value).length();
    }

    public static float getClamped(float value, float min, float max) {
        if (min > max) {
            System.err.println("clamp: min > max!");
        }
        return Math.max(Math.min(value, max), min);
    }

    public static int getClamped(int value, int min, int max) {
        return (int) getClamped((float) value, min, max);
    }

    public static Vec3 getDeltaMovement(Vec3 offset, Vec3 rotation) {
        Vec3 result = new Vec3();
        // left/right
        if (offset.x != 0f) {
            int direction = (int) Math.signum(offset.x);
            float dx = (float) (Math.cos(rotation.y + Misc.PI) * direction);
            float dz = (float) (Math.sin(rotation.y + Misc.PI) * direction);
            result = result.add(new Vec3(dx, 0, dz));
        }
        // up/down
        if (offset.y != 0f) {
            result = result.add(new Vec3(0, offset.y, 0));
        }
        // forwards/backwards
        if (offset.z != 0f) {
            int direction = (int) Math.signum(offset.z);
            float dx = (float) (Math.cos(rotation.y - Misc.PI / 2) * direction);
            float dz = (float) (Math.sin(rotation.y - Misc.PI / 2) * direction);
            result = result.add(new Vec3(dx, 0, dz));
        }
        return result;
    }

    public static Vec3 getDirection(Vec3 source, Vec3 destination) {
        return destination.sub(source).normalize();
    }

    public static Vec3 getDownVector() {
        return new Vec3(0, -1, 0);
    }

    public static Vec3 getForwardVector(Vec3 rotation) {
        float x = (float) (Math.sin(rotation.y) * Math.cos(rotation.x));
        float y = -(float) (Math.sin(rotation.x));
        float z = -(float) (Math.cos(rotation.y) * Math.cos(rotation.x));

        return new Vec3(x, y, z).normalize();
    }

    public static Vec3 getLeftVector() {
        return new Vec3(-1, 0, 0);
    }

    private static Matrix4f getMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projMatrix) {
        Matrix4f m = new Matrix4f(modelMatrix);
        Matrix4f v = new Matrix4f(viewMatrix);
        Matrix4f p = new Matrix4f(projMatrix);

        Matrix4f mvp = new Matrix4f();

        v.mul(m, mvp);
        p.mul(mvp, mvp);

        return mvp;
    }

    public static Vec3 getOppositeVector(Vec3 vector) {
        return vector.negated();
    }

    public static Vec3 getRightVector() {
        return new Vec3(1, 0, 0);
    }

    public static Vec3 getUpVector() {
        return new Vec3(0, 1, 0);
    }

    public static boolean isInRange(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static float lerp(float v0, float v1, float step) {
        return (1 - step) * v0 + step * v1;
    }

    public static Vec3 lerp(Vec3 v0, Vec3 v1, float step) {

        return new Vec3((1 - step) * v0.x + step * v1.x, (1 - step) * v0.y + step * v1.y,
                (1 - step) * v0.z + step * v1.z);
    }

    public static double normalizeScreenCoordX(double x) {
        x /= Window.instance.getWidth();
        x = x * 2 - 1;
        return x;
    }

    public static double normalizeScreenCoordY(double y) {
        y /= Window.instance.getHeight();
        y = -(y * 2 - 1);
        return y;
    }

    public static void printAngles(Vec3 rotation) {
        int x = (int) (rotation.x * 180f / Misc.PI);
        int y = (int) (rotation.y * 180f / Misc.PI);
        // int z = (int) (rotation.z * 180f / Misc.PI);
        System.out.println("up: " + x + ", side: " + y);
    }

    public static float random() {
        return (float) Math.random();
    }

    public static float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    public static int random(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }

    public static Vec3 randomVec3(float min, float max) {
        return new Vec3(random(min, max), random(min, max), random(min, max));
    }

    public static float sin(float x) {
        return (float) Math.sin(x);
    }

    public static float tan(float x) {
        return (float) Math.tan(x);
    }

    // TODO calculate total rotation needed for an object facing -Z to face the destination point
    public static Vec3 getRotation(Vec3 position, Vec3 destination) {
        return new Vec3();
    }

    // TODO get angle between direction and position
    public static float getAngle(Vec3 direction, Vec3 position) {
        float angle = 0;

        return angle;
    }

    public static float toRadians(float angle) {
        return (float) Math.toRadians(angle);
    }

    public static float toDegrees(float angle) {
       return (float)Math.toDegrees(angle);
    }

}
