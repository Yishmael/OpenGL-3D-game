package game.utils;

import java.util.ArrayList;

import engine.utils.Vec3;
import game.World;
import game.entities.Entity;
import game.entities.objects.Projectile;
import game.entities.units.Unit;

public abstract class Collision {
    // private static Player player = Player.instance;

    public static void update() {
        // TODO remove this mess
        checkCollisions(World.units, new ArrayList<>(World.staticObjects));
        checkCollisions(World.units, new ArrayList<>(World.dynamicObjects));
    }

    private static void checkCollisions(ArrayList<Unit> units, ArrayList<Entity> objects) {
        for (Unit unit: units) {
            float landingHeight = -100;
            ArrayList<AABB> boxes1 = unit.getBoundingBoxes();
            for (AABB box1: boxes1) {
                for (Projectile p: World.projectiles) {
                    if (p.isDestroyed()) {
                        continue;
                    }
                    for (AABB pBox: p.getBoundingBoxes()) {
                        if (pBox.collidesWith(box1)) {
                            p.onCollision(unit);
                            unit.onCollision(p);
                            break;
                        }
                    }
                }
            }
            for (Entity obj: objects) {
                ArrayList<AABB> boxes2 = obj.getBoundingBoxes();
                if (obj == unit) {
                    continue;
                }
                for (AABB box1: boxes1) {
                    for (AABB box2: boxes2) {
                        if (box2.collidesWith(box1)) {
                            unit.onCollision(obj);
                            obj.onCollision(unit);
                            if (box1.canStepOn(box2)) {
                                landingHeight = box2.getMaxY();
                                unit.getTransform().getPosition().y = box2.getMaxY() + box1.getHeight() / 2;
                            } else {
                                // temporary collision resolving
                                Vec3 vec = box1.getGlobalCenter().sub(box2.getGlobalCenter());
                                vec.y = 0;
                                vec.normalize();
                                unit.getPosition().set(unit.getPosition().add(vec.mul(0.2f)));
                            }
                        }
                        if (box1.isDirectlyAbove(box2)) {
                            // System.out.println(e1.getName() + " above " + e2.getName());
                            if (box2.getMaxY() > unit.getLandingHeight()) {
                                // System.out.println(box2.getMaxY());
                                landingHeight = box2.getMaxY();
                                // System.out.println("lh" + landingHeight);
                                // System.out.println(box2.getMaxY());
                            }
                        }
                        for (Projectile p: World.projectiles) {
                            if (p.isDestroyed()) {
                                continue;
                            }
                            for (AABB pBox: p.getBoundingBoxes()) {
                                if (pBox.collidesWith(box2)) {
                                    p.onCollision(obj);
                                    obj.onCollision(p);
                                }
                            }
                        }
                    }
                }
            }
            if (landingHeight > -100) { // it updated this frame
                unit.setLandingHeight(landingHeight);
                landingHeight = -100;
                unit.checkFlying();
            } else {
                unit.setLandingHeight(0);
            }
            /*
             * landingHeight = max y coordinate below unit's feet
             */
        }

    }
}
