package engine.components.colliders;

import engine.GameObject;
import engine.physics.IntersectInfo;
import engine.physics.IntersectInfo.ColliderType;
import engine.utils.Vec3;
import game.Misc;
import game.data.spatial.Point;

public class Plane implements Collider {

    private GameObject go;
    private Vec3 localPosition = new Vec3();
    private Vec3 normal = new Vec3();

    public Plane(GameObject go) {
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
        case SPHERE: // plane to sphere
            Sphere sphere = ((Sphere) other);
            float height = getGlobalPosition().sub(sphere.getGlobalCenter()).dot(getNormal());
            float netDistance = height - sphere.getRadius();
            intersected = Misc.isInRange(netDistance, -sphere.getRadius(), 0);
            info = new IntersectInfo(intersected, netDistance);
            break;
        case PLANE:
            info = new IntersectInfo(false, 999);
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

    }

    @Override
    public void onCollisionEnter() {

    }

    @Override
    public void onCollisionExit() {
    }

    public void setNormal(Vec3 normal) {
        this.normal = normal.normalize();
    }

    @Override
    public String toString() {
        return "Plane: " + "hor" + ", p: " + localPosition;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Vec3 getGlobalPosition() {
        return new Vec3(go.getTransform().getPosition().add(localPosition));
    }

    public Vec3 getLocalPosition() {
        return localPosition;
    }

    public void setRelativePosition(Vec3 position) {
        this.localPosition = position;
        go.getTransform().translate(position);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public ColliderType getColliderType() {
        return ColliderType.PLANE;
    }

    @Override
    public void draw() {

    }

    public float getPlaneAngle() {
        return (float) Math.acos(normal.dot(new Vec3(0, 1, 0)));
    }

    @Override
    public Vec3 getNormalAtPoint(Point point) {
        return normal;
    }

}
