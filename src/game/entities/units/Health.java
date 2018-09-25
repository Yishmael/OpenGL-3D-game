package game.entities.units;

import game.time.Timer;

public class Health {

    int current, max;
    float lastTick;
    private boolean changed = true;

    public Health(int current, int max) {
        this.current = current;
        this.max = max;
    }
    
    public int getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return current + "/" + max;
    }

    public void delta(int delta) {
        current = Math.min(Math.max(0, current + delta), max);
        changed = true;
    }

    public boolean isZero() {
        return current == 0;
    }

    public boolean hasChanged() {
        if (changed) {
            changed = false;
            return true;
        }
        return false;
    }

    public void tick() {
        if (Timer.now() - lastTick > 3f) {
            lastTick = Timer.now();

            delta(1);
        }
    }
}
