package game.graphics.particles;

import org.joml.Vector4f;

import engine.utils.Color;
import engine.utils.Vec3;
import game.Misc;
import game.time.Timer;
import game.utils.Transform;

public class Fragment {
    private Transform initialTransform, transform;
    private float creationTime;
    private Color color = new Color(1, 1, 1, 1);

    public Fragment(Transform transform) {
        this.transform = new Transform(transform);
        this.initialTransform = new Transform(transform);

        creationTime = Timer.now();
    }

    public Transform getTransform() {
        return transform;
    }

    public float getCreationTime() {
        return creationTime;
    }

    public void deltaOpacity(float delta) {
        color.w = Math.min(Math.max(color.w + delta, 0), 1);
    }

    private void resetTransform() {
        transform = new Transform(initialTransform);
    }

    private void resetColor() {
        color.x = Misc.random(0.7f, 1.3f);
        color.y = Misc.random(0.7f, 1.3f);
        color.z = Misc.random(0.7f, 1.3f);
        color.w = 1;
    }

    private void resetTime() {
        creationTime = Timer.now();
    }

    public void reset() {
        resetTransform();
        resetColor();
        resetTime();
    }

    public Vector4f getColor() {
        return color;
    }

    public float getOpacity() {
        return color.w;
    }

    public boolean hasFinished() {
        return finished;
    }

    private boolean finished;

    public void finish() {
        finished = true;
    }
}
