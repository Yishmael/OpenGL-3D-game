package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import engine.components.Camera;
import engine.components.Component;
import engine.components.Component.ComponentType;
import engine.components.MeshRenderer;
import engine.components.TransformComponent;
import engine.components.colliders.Collider;
import engine.scripts.CollisionResponse;
import engine.utils.Vec3;
import game.time.Timer;
import game.utils.Transform;

public class GameObject {
    public enum PrimitiveType {
        CIRCLE,
        CUBE,
        ICOSPHERE,
        PLANE,
        PYRAMID,

        // TEMP
        // statics
        BARREL,
        BUILDING,
        DOME,
        HALL,
        HOUSE,
        MAZE,
        PIT,
        RAMP,
        STAIRS,
        TARGET,
        TEMPLE,
        TOWER,
        TREE,
        TUNNEL,
        WALL,
        CAVE,
        DUNGEON,
        BUNKER,
        VACANT,
        HANGAR,

        // movable objects
        BULLET,
        ELEVATOR,

        // items
        KNIFE,
        PISTOL,
        PLANK,
        RIFLE,
        SHOTGUN,
        AXE,
        GRENADELAUNCHER,

        // units
        HUMAN,
        PLAYER,
        
        // interactives
        LADDER, 
    }

    private ArrayList<Component> comps = new ArrayList<>();
    private boolean destroyed;
    private String name = "UNNAMED";
    private float lifetime, creationTime;

    public GameObject(String name) {
        this(name, Transform.origin);
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        addComponent(TransformComponent.class);
        TransformComponent trans = (TransformComponent) getComponent(ComponentType.TRANSFORM);
        trans.getTransform().set(transform);
        addComponent(MeshRenderer.class);
        creationTime = Timer.now();
    }

    public void addComponent(Class<?> compClass) {
        try {
            Constructor<?> constr = compClass.getConstructor(GameObject.class);
            Component comp = (Component) constr.newInstance(this);
            comps.add(comp);
        } catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        destroyed = true;
    }

    public Component getComponent(ComponentType type) {
        for (Component comp: comps) {
            if (comp.getType() == type) {
                return comp;
            }
        }
        return null;
    }

    public boolean hasComponent(ComponentType type) {
        for (Component comp: comps) {
            if (comp.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Transform getTransform() {
        return ((TransformComponent) getComponent(ComponentType.TRANSFORM)).getTransform();
    }

    public Vec3 getPosition() {
        return getTransform().getPosition();
    }

    public Vec3 getRotation() {
        return getTransform().getRotation();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void sendMessage(String message) {
        for (Component comp: comps) {
            comp.addMessage(message);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String result = "\n";
        for (Component comp: comps) {
            result += "  " + comp + "\n";
        }
        return name + ": " + getTransform().getPosition() + result;
    }

    public Vec3 path;

    public void update(float dt) {
        for (Component comp: comps) {
            comp.update(dt);
        }
        // TODO remove asap!
        if (path != null) {
            getTransform().getPosition().add(path.mul(dt * 10));
        }
        // TODO handle this elsewhere
        if (Timer.now() - creationTime > lifetime && lifetime > 0) {
            destroy();
        }
    }

    public void draw() {
        for (Component comp: comps) {
            comp.draw();
        }
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    // TODO remove this
    public CollisionResponse cr;

    public void onCollision(Collider other) {
        if (cr != null) {
            if (cr.triggersAt(other.getAttachedGameObject().getName())) {
                cr.execute(this);
            }
        }
    }

    public Camera getCamera() {
        return ((Camera) getComponent(ComponentType.CAMERA));
    }
}
