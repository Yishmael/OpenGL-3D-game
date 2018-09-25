package engine.physics;

import java.util.ArrayList;

import engine.GameObject;
import engine.components.Component.ComponentType;
import engine.components.RigidBody;
import engine.components.colliders.Collider;
import engine.utils.Vec3;

public class PhysicsEngine {
    public static Vec3 GRAVITY = new Vec3(0, -10, 0);
    public static final float GROUND_LEVEL = -444444;

    public static void simulateAll(ArrayList<GameObject> gameObjects, float dt) {
        for (GameObject go: gameObjects) {
            if (go.hasComponent(ComponentType.RIGID_BODY)) {
                RigidBody rb = (RigidBody) go.getComponent(ComponentType.RIGID_BODY);
                if (!rb.paused() && !rb.isImmovable()) {
                    rb.integrate(dt); 
                    if (go.getTransform().getPosition().y - go.getTransform().getScale() / 2 <= GROUND_LEVEL) {
                        rb.pause();
                    }
                }
            }
        }
    }

    public static void resolveCollision(GameObject go1, GameObject go2, IntersectInfo info) {
        RigidBody rb1 = (RigidBody) go1.getComponent(ComponentType.RIGID_BODY);
        RigidBody rb2 = (RigidBody) go2.getComponent(ComponentType.RIGID_BODY);
        if (rb1.paused() && rb2.paused()) {
            return;
        }
        Collider col1 = (Collider) go1.getComponent(ComponentType.COLLIDER);
        Collider col2 = (Collider) go2.getComponent(ComponentType.COLLIDER);

        Vec3 vel1 = rb1.getLinearVelocity();
        Vec3 vel2 = rb2.getLinearVelocity();

//        if (!rb1.isImmovable()) {
//            Vector3f offset = new Vector3f(info.getBoundsDistance());
//            Vector3f direction = vel1.normalize(new Vector3f());
//            direction.mul(offset);
//            go2.getTransform().getPosition().add(direction);
//
//            float cos = vel1.normalize(new Vector3f()).dot(col2.getNormalAtPoint(new Vector3f(0, 0, 0)));
//            float sin = (float) Math.sin(Math.acos(cos));
//
//            vel1.mul(new Vector3f(sin, cos, sin).mul(0.5f));
//        }
//        if (!rb2.isImmovable()) {
//            Vector3f offset = new Vector3f(info.getBoundsDistance());
//            Vector3f direction = vel2.normalize(new Vector3f());
//            direction.mul(offset);
//            go2.getTransform().getPosition().add(direction);
//
//            float cos = vel2.normalize(new Vector3f()).dot(col1.getNormalAtPoint(new Vector3f(0, 0, 0)));
//            float sin = (float) Math.sin(Math.acos(cos));
//
//            vel2.mul(new Vector3f(sin, cos, sin).mul(0.5f));
//        }
        go2.getPosition().add(vel2.normalized().mul(info.getBoundsDistance()));
        
        rb1.pause();
        rb2.pause();
        
        

        // TODO resolve fully elastic collision
        // m1v1 + m2v2 = m1v1` + m2v2`
        // float m1 = rb1.getMass();
        // float m2 = rb2.getMass();
        //
        // Vector3f v1 = rb1.getVelocity();
        // Vector3f v2 = rb2.getVelocity();

    }
}
