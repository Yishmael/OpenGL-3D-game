package engine.components;

import engine.GameObject;
import engine.physics.Forcekeeper;
import engine.physics.PhysicsEngine;
import engine.utils.Vec3;
import game.time.Timer;

public class RigidBody implements Component {
    private GameObject go;

    private float mass;
    private Vec3 prevPosition = new Vec3();
    private boolean immovable, paused;
    private float lastTimePositionChecked;
    private Vec3 linearVelocity = new Vec3();
    private Vec3 acceleration = PhysicsEngine.GRAVITY;
    private Vec3 force = new Vec3();
    private Forcekeeper forcekeeper = new Forcekeeper();

    public RigidBody(GameObject go) {
        this.go = go;
    }

    @Override
    public void addMessage(String message, Object... args) {
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    @Override
    public GameObject getAttachedGameObject() {
        return go;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.RIGID_BODY;
    }

    @Override
    public String toString() {
        return "RigidBody: " + mass + "kg" + " v:" + linearVelocity;
    }

    @Override
    public void update(float dt) {
        if (paused) {
            return;
        }
        checkPauseConditions();
    }

    private void checkPauseConditions() {
        if (Timer.now() - lastTimePositionChecked > 0.5f) {
            lastTimePositionChecked = Timer.now();
            float distance = go.getTransform().getPosition().distanceTo(prevPosition);
            prevPosition = new Vec3(go.getTransform().getPosition());
            if (distance < 0.01f) {
                pause();
            }
        }
    }

    public boolean paused() {
        return paused;
    }

    public void integrate(float dt) {
        force = forcekeeper.getForce();
        acceleration.add(force.mul(1 / mass));
        go.getTransform().getPosition().add(linearVelocity.mul(dt).add(acceleration.mul(dt * dt / 2)));
        linearVelocity.add(acceleration.mul(dt));
        forcekeeper.clear();
    }

    public void pause() {
        paused = true;
        if (!go.getName().equals("Plane1")) {
            System.out.println(go.getName() + " paused");
        }
    }

    public void applyImpulse(Vec3 impulse) {
        unpause();
        linearVelocity.add(impulse.mul(1 / mass));
    }

    public void unpause() {
        paused = false;
    }

    @Override
    public void draw() {

    }

    public Vec3 getLinearVelocity() {
        return linearVelocity;
    }

    public void setImmovable(boolean immovable) {
        this.immovable = immovable;
    }

    public boolean isImmovable() {
        return immovable;
    }

}
