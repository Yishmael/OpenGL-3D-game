package engine.physics;

import game.Misc;

public class IntersectInfo {

    public enum ColliderType {
        SPHERE, PLANE;
    }

    private boolean intersects;
    private float boundsDistance;
    private float impactAngle;

    public IntersectInfo(boolean intersects, float boundsDistance) {
        this.intersects = intersects;
        this.boundsDistance = boundsDistance;
    }

    public void setImpactAngle(float impactAngle) {
        this.impactAngle = impactAngle;
    }

    public float getImpactAngle() {
        return impactAngle;
    }

    public boolean intersected() {
        return intersects;
    }

    public float getBoundsDistance() {
        return boundsDistance;
    }

    @Override
    public String toString() {
        return "Intersects: " + intersects + ", dist: " + boundsDistance + ", ang: " + impactAngle * 180f / Misc.PI;
    }

}
