package game.entities.behaviours;

import engine.utils.Vec3;
import game.Misc;
import game.utils.Transform;

public class Rotator {

    private float maxAngle;
    private Transform transform;
    private Vec3 axis;
    private boolean looping;
    private boolean finished;
    private float speed = 0.02f;
    private float totalAngle;

    public Rotator(Transform transform, Vec3 axis, float maxAngle, boolean looping) {
        this.transform = transform;
        this.axis = axis;
        this.maxAngle = maxAngle;
        this.looping = looping;
    }

    public void update() {
        if (finished) {
            return;
        }
        // Y rotation only
        float delta = speed * Math.signum(axis.y);
        transform.rotateY(delta);
        totalAngle += delta;
        if (totalAngle > maxAngle || totalAngle < 0) {
            if (!looping) {
                finished = true;
                return;
            }
            axis.negate();
        }
    }

}
