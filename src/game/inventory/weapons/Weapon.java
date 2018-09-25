package game.inventory.weapons;

import org.joml.Matrix4f;

import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.meshes.TexturedModel;
import game.time.Timer;
import game.utils.Transform;

public abstract class Weapon {

    protected enum State {
        ACTIVE, IDLE,
    }

    public enum WeaponType {
        PISTOL, RIFLE, KNIFE, GRENADELAUNCHER,
    }

    protected TexturedModel model;
    protected Transform transform;
    protected Vec3 initialPosition = new Vec3(0.6f, -0.55f, 0);
    protected float lastTimeUsed;
    protected float attackRate;
    protected Vec3 offset = new Vec3();
    protected String tag;
    protected Vec3 origin, forward;
    protected WeaponType type;

    public Weapon(WeaponType type) {
        this.type = type;

        if (type == WeaponType.KNIFE) {
            initialPosition = new Vec3(0.85f, -1.05f, -1f);
        } else if (type == WeaponType.RIFLE) {
            initialPosition = new Vec3(0.55f, -0.65f, -0.7f);
        } else if (type == WeaponType.PISTOL) {
            initialPosition = new Vec3(0.55f, -0.85f, -0.55f);
        } else if (type == WeaponType.GRENADELAUNCHER) {
            initialPosition = new Vec3(2.2f, -2.95f, -0.85f);
        }

        transform = new Transform(new Vec3(initialPosition), new Vec3(0, (90 + 0) * Misc.PI / 180, 0), 1);
    }

    // temp
    public void deltaInitialPosition(Vec3 delta) {
        initialPosition = initialPosition.add(delta);
        System.out.println(initialPosition);
        transform = new Transform(new Vec3(initialPosition), new Vec3(0, (90 + 0) * Misc.PI / 180, 0), 1);
    }

    public TexturedModel getModel() {
        return model;
    }

    public Transform getTransform() {
        return transform;
    }

    public boolean isReady() {
        return Timer.now() - lastTimeUsed > 1 / attackRate;
    }

    public void prepareForDrawing() {
        model.bind();
        model.getMesh().getShader().uploadMatrix4f("u_vMatrix", new Matrix4f());
        model.getMesh().getShader().uploadMatrix4f("u_mMatrix", transform.getMatrix());
    }

    // temp
    public abstract void reload();

    public void setAttackRate(float attackRate) {
        this.attackRate = attackRate;
    }

    public void setData(String tag, Vec3 origin, Vec3 forward) {
        this.tag = tag;
        this.origin = origin;
        this.forward = forward;

        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = forward.cross(up).normalize();

        origin.add(right.mul(0.15f));
        origin.add(up.mul(-0.05f));
        origin.add(forward.mul(0.55f));

        origin.y += CoreCamera.eyeHeightFromCenter; // camera position

    }

    public abstract void startAnimation();

    @Override
    public abstract String toString();

    public abstract void update();

    public abstract void changeMode();

    public abstract void use();
}
