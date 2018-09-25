package engine.components;

import org.joml.Matrix4f;

import engine.GameObject;
import game.Misc;
import game.utils.Transform;

public class Camera implements Component {
    private GameObject go;
    private Transform transform;
    private Matrix4f projMatrix;
    private float fov = 100, zNear = 0.01f, zFar = 1000f;

    public Camera(GameObject go) {
        this.go = go;
        this.transform = go.getTransform();
        this.projMatrix = Misc.createPerspectiveMatrix(fov, zNear, zFar);

    }

    @Override
    public void addMessage(String message, Object... args) {

    }

    @Override
    public void draw() {

    }
    
    @Override public GameObject getAttachedGameObject() {
        return go;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }
    
    public Transform getTransform() {
        return transform;
    }
    
    @Override
    public ComponentType getType() {
        return ComponentType.CAMERA;
    }

    @Override
    public void update(float dt) {
        this.transform = go.getTransform();
    }

}
