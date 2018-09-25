package engine.components.colliders;

import engine.GameObject;
import engine.physics.IntersectInfo;
import engine.physics.IntersectInfo.ColliderType;
import engine.utils.Vec3;
import game.Misc;
import game.data.spatial.Point;

public class Sphere implements Collider {

    private GameObject go;
    private Vec3 localCenter = new Vec3();
    private float radius;

    public Sphere(GameObject go) {
        this.go = go;
    }

    @Override
    public void addMessage(String message, Object... args) {
    }

    @Override
    public GameObject getAttachedGameObject() {
        return go;
    }

    @Override
    public IntersectInfo getIntersectInfo(Collider other) {
        ColliderType type = other.getColliderType();
        IntersectInfo info = null;
        boolean intersected = false;

        switch (type) {
        case SPHERE: // sphere to sphere
            Sphere sphere = ((Sphere) other);
            float centerDistance = Math.abs(sphere.getGlobalCenter().sub(getGlobalCenter()).length());
            float boundsDistance = centerDistance - radius - sphere.radius;
            intersected = Misc.isInRange(boundsDistance, -radius - sphere.radius, 0);
            info = new IntersectInfo(intersected, boundsDistance);
            break;
        case PLANE: // sphere to plane
            Plane plane = ((Plane) other);
            float height = getGlobalCenter().sub(plane.getGlobalPosition()).dot(plane.getNormal());
            float netDistance = height - radius;
            intersected = Misc.isInRange(netDistance, -radius, 0);
            info = new IntersectInfo(intersected, netDistance);
            break;
        }
        return info;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.COLLIDER;
    }

    @Override
    public void onCollision(Collider collider) {
        go.onCollision(collider);

    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void onCollisionEnter() {
        // TODO maybe remove this

    }

    @Override
    public void onCollisionExit() {
    }

    public Vec3 getGlobalCenter() {
        return new Vec3(go.getTransform().getPosition().add(localCenter));
    }

    public Vec3 getLocalCenter() {
        return localCenter;
    }

    public void setRelativePosition(Vec3 position) {
        this.localCenter = position;
        go.getTransform().translate(position );
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Sphere: " + localCenter + " r: " + radius;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public ColliderType getColliderType() {
        return ColliderType.SPHERE;
    }

    @Override
    public void draw() {
        // TODO draw collider box here

    }

    @Override
    public Vec3 getNormalAtPoint(Point point) {
        // TODO use point of LOCAL space
        Vec3 pos = new Vec3(point.x, point.y, point.z);
        return pos.sub(new Vec3(go.getTransform().getPosition()).normalized());
    }

}
