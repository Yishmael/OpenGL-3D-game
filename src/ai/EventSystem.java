package ai;

import java.util.ArrayList;
import java.util.Iterator;

import game.World;
import game.entities.Entity;
import game.entities.units.Unit;
import game.time.Timer;

public abstract class EventSystem {

    private static ArrayList<Event> events = new ArrayList<>();

    public static void reportGlobalEvent(Event event) {
        events.add(event);
    }

    private static float lastUpdateTime;

    public static void update() {
        if (Timer.now() - lastUpdateTime > 1f / 3) {
            lastUpdateTime = Timer.now();
            for (Iterator<Event> iter = events.iterator(); iter.hasNext();) {
                Event event = iter.next();

                for (Unit unit: World.units) {
                    if (unit.getAgent() != null) {
                        unit.getAgent().processEvent(event);
                    }
                }

                iter.remove();
            }
        }
    }

}
