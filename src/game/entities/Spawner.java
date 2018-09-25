package game.entities;

import java.awt.Event;
import java.util.ArrayList;

public class Spawner {

    // spawns all of the units in the level
    // called once per level load
    public static ArrayList<Entity> generateUnits(int level) {
        ArrayList<Entity> units = new ArrayList<>();

        switch (level) {
        case 1:
            // units.add(new Spider(new Vec3(20, 156, 53))));
            // ...
            break;
        }
        return units;
    }

    // returnss a list of units that spawn when a given event is triggered at a particular level
    public static ArrayList<Entity>spawnUnits(int level, Event event) {
        ArrayList<Entity> units = new ArrayList<>();

        return units;
    }
    
}
