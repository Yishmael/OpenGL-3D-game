package game;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import ai.Agent;
import engine.GameObject.PrimitiveType;
import engine.scripts.Teleporter;
import game.entities.DynamicObject;
import game.entities.Entity;
import game.entities.StaticObject;
import game.entities.interatives.Interactive;
import game.entities.interatives.Ladder;
import game.entities.objects.Projectile;
import game.entities.objects.powerups.LootableItem;
import game.entities.objects.powerups.LootableItem.LootableType;
import game.entities.units.Enemy;
import game.entities.units.Player;
import game.entities.units.Unit;
import game.graphics.HUD;
import game.graphics.Skybox;
import game.graphics.Skybox.SkyboxType;
import game.inventory.weapons.Cone;
import game.meshes.Texture.TextureType;

public abstract class World {

    public enum WorldType {
        TEST, DEFAULT;
    }

    public static boolean showingBoundingBoxes = false;

    public static ArrayList<Unit> units = new ArrayList<>();
    public static ArrayList<DynamicObject> dynamicObjects = new ArrayList<>();
    public static ArrayList<StaticObject> staticObjects = new ArrayList<>();

    public static ArrayList<Projectile> projectiles = new ArrayList<>();

    public static Cone interactionCone = new Cone();
    public static Cone knifeCone;

    public static void addDynamicObject(DynamicObject object) {
        dynamicObjects.add(object);
    }

    public static void addProjectile(Projectile proj) {
        projectiles.add(proj);
    }

    public static void addStaticObject(StaticObject object) {
        staticObjects.add(object);
    }

    public static void addStaticObjects(ArrayList<StaticObject> objects) {
        staticObjects.addAll(objects);
    }

    public static void addUnit(Unit unit) {
        units.add(unit);
    }

    public static void init() {
        WorldType type = WorldType.TEST;
        if (type == WorldType.TEST) {
            staticObjects.add(new StaticObject(PrimitiveType.CUBE, TextureType.ROCK, null, 0, 0, 0, 0, 0, 0, 1f));

            staticObjects.add(new StaticObject(PrimitiveType.CUBE, TextureType.DIRT, null, 0, 2, 0, 0, 0, 0, 1f));
            staticObjects.add(new StaticObject(PrimitiveType.TARGET, TextureType.RUST, null, 5, 0, 4, 0, 0, 0, 1f));
            staticObjects.add(new StaticObject(PrimitiveType.TARGET, TextureType.WOOD, null, 5, 0, 6, 0, 0, 0, 1f));
            dynamicObjects.add(new LootableItem(LootableType.RIFLE, PrimitiveType.RIFLE, null, 2, 1f, 3));
            dynamicObjects.add(new LootableItem(LootableType.KNIFE, PrimitiveType.KNIFE, null, 5, 1f, 3));
            staticObjects.add(new Ladder(PrimitiveType.LADDER, TextureType.METAL, null, -5, 0, 3, 0, 0, 0, 1));
            for (int i = 0; i < 3; i++) {
                dynamicObjects
                        .add(new LootableItem(LootableType.HEALTHPACK, PrimitiveType.ICOSPHERE, null, 3, 1f, 3 - i));
            }
            for (int i = 0; i < 3; i++) {
                staticObjects.add(new StaticObject(PrimitiveType.PLANE, TextureType.STONE, null, -2 * i - 3,
                        i / 2f * 0.9f, 0, 0, 0, 0, 1));
            }
            for (int i = 0; i < 1; i++) {
                units.add(new Enemy(PrimitiveType.HUMAN, TextureType.WOOD, null, -2 + i / 10, 0, -2 - -2 * (i % 10)));
            }
            dynamicObjects.add(new DynamicObject(PrimitiveType.ELEVATOR, TextureType.WOOD, null, 7, 0, 6, 0, 0, 0, 1));
        } else if (type == WorldType.DEFAULT) {
        }
    }

    public static void update() {

        interactionCone = new Cone(Player.instance.getPosition(), Misc.getForwardVector(Player.instance.getRotation()),
                Player.instance.getName(), 1.6f);
        boolean objectFound = false;
        for (Entity object: World.staticObjects) {
            if (object instanceof Interactive) {
                if (interactionCone.reaches(object.getPosition())) {
                    HUD.setInteractiveObject((Interactive) object);
                    objectFound = true;
                    break;
                }
            }
        }
        if (!objectFound) {
            HUD.clearInteractiveObject();
        }
        for (Iterator<Unit> iter = units.iterator(); iter.hasNext();) {
            Unit unit = iter.next();
            unit.update();
            if (knifeCone != null) {
                if (knifeCone.reaches(unit.getPosition())) {
                    unit.getHealth().delta(-30);
                    System.out.println("Knife stabbed " + unit.getName());
                }
                knifeCone = null;
            }
            if (unit.isDestroyed()) {
                iter.remove();
            }
        }

        for (Iterator<DynamicObject> iter = dynamicObjects.iterator(); iter.hasNext();) {
            DynamicObject dynObj = iter.next();
            dynObj.update();
            if (dynObj.isDestroyed()) {
                iter.remove();
            }

        }
        for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext();) {
            Projectile p = iter.next();
            p.update();
            if (p.isDestroyed()) {
                iter.remove();
            }
        }

        {
            int err;
            while ((err = GL11.glGetError()) != GL11.GL_NO_ERROR) {
                System.out.println(err);
            }
        }

    }

}
