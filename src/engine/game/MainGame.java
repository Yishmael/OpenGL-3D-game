package engine.game;

import engine.GameObject;
import engine.Prefabs;
import engine.components.Camera;
import engine.components.Component.ComponentType;
import engine.components.Input;
import engine.components.RigidBody;
import engine.components.colliders.Sphere;
import engine.utils.Vec3;

public class MainGame extends Game {

    public MainGame() {
        super(800, 600);
    }

    @Override
    public void start() throws InterruptedException {
        GameObject cube = Prefabs.createCube();
        
        cube.getTransform().setPosition(new Vec3(0, 5.5f, 0));
//        cube.getTransform().rotate(new Vector3f(0.5f, 0.7f, 0));
        
        cube.addComponent(Input.class);
        
        cube.addComponent(RigidBody.class);
        RigidBody rb = (RigidBody) cube.getComponent(ComponentType.RIGID_BODY);
        rb.setMass(2);
        
        cube.addComponent(Sphere.class);
        Sphere sphere = (Sphere) cube.getComponent(ComponentType.COLLIDER);
        sphere.setRadius(0.5f);
        
        cube.addComponent(Camera.class);

        addObject(cube);

        super.start();
    }

    public static void main(String[] args) throws InterruptedException {
        new MainGame().start();
    }
}
