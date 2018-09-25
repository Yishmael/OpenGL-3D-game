package engine.components;

import engine.GameObject;
import game.utils.Transform;

public class TransformComponent implements Component{
    private GameObject go;
    
    private Transform transform;
    
    public TransformComponent(GameObject go) {
        this.go = go;
        this.transform = new Transform();
    }

    @Override
    public void addMessage(String message, Object... args) {
        
    }

    @Override
    public GameObject getAttachedGameObject() {
        return go;
    }
    
    public Transform getTransform() {
        return transform;
    }
    

    @Override
    public ComponentType getType() {
        return ComponentType.TRANSFORM;
    }

    @Override
    public void update(float dt) {
        
    }

    @Override
    public void draw() {
        
    }
    
}
