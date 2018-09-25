package game.entities.behaviours;

import engine.utils.Vec3;
import game.Misc;
import game.utils.Transform;

public class Translator {

    private float maxOffset;
    private Transform initialTransform, transform;
    private Vec3 direction;
    private boolean looping;
    private boolean finished;

    public Translator(Transform transform, Vec3 direction, float maxOffset, boolean looping) {
        this.transform = transform;
        this.initialTransform = new Transform(transform);
        this.direction = direction;
        this.maxOffset = maxOffset;
        this.looping = looping;
    }

    public void update() {
        if (finished) {
            return;
        }
        float difference = Math.abs(transform.getPosition().y - initialTransform.getPosition().y);
        if (difference > maxOffset) {
            if (!looping) {
                finished = true;
                return;
            }
            direction = Misc.getOppositeVector(direction);
        }
        float finalY = transform.getPosition().y + maxOffset * Math.signum(direction.y);
        transform.setY(Misc.lerp(transform.getPosition().y, finalY, 0.03f));
    }

}
