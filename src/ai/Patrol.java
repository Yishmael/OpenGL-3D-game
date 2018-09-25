package ai;

import java.util.ArrayList;

import engine.utils.Vec3;
import game.Misc;
import game.utils.Transform;

public class Patrol {

    private int currentDestinationIndex = 0;
    private ArrayList<Vec3> destinations = new ArrayList<>();
    private Transform transform;

    // TODO add update method that will automatically update the position of the entity
    public Patrol(Transform transform) {
        this.transform = transform;
        addPosition(transform.getPosition());
    }

    public void update() {
        if (!reachedDestination()) {
            transform.translate(Misc.getDirection(transform.getPosition(), getDestination()).mul(0.1f));
        }
        // optionally start moving to the next destination automatically
        else {
            next();
        }
    }

    public boolean reachedDestination() {
        return transform.getPosition().distanceTo(getDestination()) < 0.1f;
    }

    public void addPosition(Vec3 position) {
        this.destinations.add(new Vec3(position));
    }

    public Vec3 getDestination() {
        return destinations.get(currentDestinationIndex);
    }

    public void next() {
        currentDestinationIndex++;
        currentDestinationIndex %= destinations.size();
    }

    public void printPath() {
        for (Vec3 dest: destinations) {
            System.out.println(dest);
        }
    }

}
