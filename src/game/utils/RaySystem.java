package game.utils;

import java.util.ArrayList;
import java.util.Iterator;

import engine.utils.Vec3;
import game.World;
import game.entities.Entity;
import game.entities.units.Unit;

// TODO use one GPU allocation for all rays
public abstract class RaySystem {

    // TODO make private
    public static ArrayList<Ray> rays = new ArrayList<>();

    public static void castRay(Vec3 origin, Vec3 direction, String tag, float range) {
        castRay(origin, direction, tag, range, 6);
    }

    public static void castRay(Vec3 origin, Vec3 direction, String tag, float range, float duration) {
        rays.add(new Ray(origin, direction, tag, range, duration));
    }

    public static void draw() {
        for (Ray ray: rays) {
            ray.draw();
        }
    }

    public static void update() {
        for (Iterator<Ray> iter = rays.iterator(); iter.hasNext();) {
            Ray ray = iter.next();

            if (ray.getTargets().size() > 0) {
                Entity target = ray.getClosestTarget();
                if (Unit.class.isAssignableFrom(target.getClass())) {
                    Unit unitTarget = (Unit) ray.getClosestTarget();
                    if (!ray.isDestroyed() && ray.damages(target)) {
                        unitTarget.getHealth().delta(-10);
                        ray.triggerAt(unitTarget);
                        System.out.println(unitTarget.getName() + " took 10 dmg from ray (" + ray.getTag() + ")");
                    }

                }
            }
            if (ray.hasExpired() || ray.isDestroyed()) {
                iter.remove();
            }
        }
    }
}
