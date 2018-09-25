package engine.components;

import engine.GameObject;

public interface Component {

    public enum ComponentType {
        RIGID_BODY, MESH, MESH_RENDERER, COLLIDER, INPUT, CAMERA, TRANSFORM,
    }

    public void addMessage(String message, Object... args);

    public GameObject getAttachedGameObject();

    public ComponentType getType();

    @Override
    public String toString();

    public void update(float dt);

    public void draw();
}
