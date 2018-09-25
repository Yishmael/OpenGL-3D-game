package engine.physics;

import java.util.ArrayList;

import engine.GameObject;
import engine.components.Component.ComponentType;
import engine.components.colliders.Collider;

public class Collision {
    public static void detectCollisions(ArrayList<GameObject> gameObjects) {
        for (int i = 0; i < gameObjects.size(); i++) {
            Collider collider1 = (Collider) gameObjects.get(i).getComponent(ComponentType.COLLIDER);
            for (int j = i + 1; j < gameObjects.size(); j++) {
                if (!gameObjects.get(i).hasComponent(ComponentType.COLLIDER)
                        || !gameObjects.get(j).hasComponent(ComponentType.COLLIDER)) {
                    continue;
                }
                Collider collider2 = (Collider) gameObjects.get(j).getComponent(ComponentType.COLLIDER);
                IntersectInfo info = collider1.getIntersectInfo(collider2);
                if (info.intersected()) {
                    // System.out.println(info);
                    // System.out.println(collider1);
                    // System.out.println(collider2);
                    collider1.onCollision(collider2);
                    collider2.onCollision(collider1);
                    PhysicsEngine.resolveCollision(gameObjects.get(i), gameObjects.get(j), info);
                }
            }
        }
    }
}
