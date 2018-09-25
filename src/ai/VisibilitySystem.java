package ai;

import ai.Event.EventType;
import game.Misc;
import game.World;
import game.entities.Entity;
import game.entities.units.Player;
import game.entities.units.Unit;
import game.time.Timer;
import game.utils.Ray;
import game.utils.RaySystem;

public abstract class VisibilitySystem {
    private static float lastUpdateTime;

    public static void update() {
        if (Timer.now() - lastUpdateTime > 1f / 3) {
            lastUpdateTime = Timer.now();
            for (Unit unit: World.units) {
                if (unit.equals(Player.instance)) {
                    continue;
                }
                RaySystem.castRay(unit.getPosition(),
                        Misc.getDirection(unit.getPosition(), Player.instance.getPosition()), "Vision", 15, 0.3f);

                Ray ray = RaySystem.rays.get(RaySystem.rays.size()-1);
                
                if (ray.getTargets().size() > 0) {
                    if (ray.getClosestTarget().equals(Player.instance)) {
                        System.out.println(unit.getName() + " spotted the player");
                        if (unit.getAgent() != null) {
                            unit.getAgent().processEvent(new Event(EventType.PLAYER_SEEN));
                        }
                        // EventSystem.reportEvent(new Event(EventType.PLAYER_SEEN));
                    }
                }

            }
        }

    }
}
