package engine.components;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import engine.CoreEngine;
import engine.GameObject;
import engine.Prefabs;
import engine.utils.Vec3;
import game.graphics.Window;

public class Input implements Component {

    private GameObject go;
    private GLFWKeyCallback keyCallback;

    public Input(GameObject go) {
        this.go = go;
        keyCallback = GLFWKeyCallback.create(new GLFWKeyCallbackI() {

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS) {
                    GameObject proj = Prefabs.createProjectile();
                    proj.getTransform().setPosition(go.getTransform().getPosition());
                    proj.path = new Vec3(1, 0, 0);
                    CoreEngine.gameObjects.add(proj);
                    // TEMP
                } else if (key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
                    Vec3 impulse = new Vec3(0, 15, 0);
                    if (go.hasComponent(ComponentType.RIGID_BODY)) {
                        ((RigidBody) go.getComponent(ComponentType.RIGID_BODY)).applyImpulse(impulse);
                    } else {
                        go.getTransform().translate(impulse.normalize());

                    }
                }
            }
        });
        GLFW.glfwSetKeyCallback(Window.instance.getWindow(), keyCallback);
    }

    public static boolean isKeyDown(int key) {
        boolean pressed = GLFW.glfwGetKey(Window.instance.getWindow(), key) == GLFW.GLFW_PRESS;
        boolean down = GLFW.glfwGetKey(Window.instance.getWindow(), key) == GLFW.GLFW_REPEAT;
        return pressed || down;
    }

    public static boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(Window.instance.getWindow(), key) == 2;
    }

    @Override
    public void addMessage(String message, Object... args) {

    }

    @Override
    public GameObject getAttachedGameObject() {
        return go;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.INPUT;
    }

    @Override
    public void update(float dt) {
        Vec3 impulse = new Vec3();

        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            impulse.z = -1;
        } else if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            impulse.z = 1;
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            impulse.x = -1;
        } else if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            impulse.x = 1;
        }
        if (impulse.length() != 0) {
            if (go.hasComponent(ComponentType.RIGID_BODY)) {
                RigidBody rb = (RigidBody) go.getComponent(ComponentType.RIGID_BODY);
                rb.unpause();
                rb.applyImpulse(impulse);
            } else {
                // force.z = -force.y;
                // force.y = 0;
                go.getTransform().translate(impulse.mul(dt * 2));
            }
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_UP)) {
            Camera camera = go.getCamera();
            camera.getTransform().rotate(new Vec3(-1f / 60, 0, 0));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            Camera camera = go.getCamera();
            camera.getTransform().rotate(new Vec3(1f / 60, 0, 0));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            Camera camera = go.getCamera();
            camera.getTransform().rotate(new Vec3(0, -1f / 60, 0));
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            Camera camera = go.getCamera();
            camera.getTransform().rotate(new Vec3(0, 1f / 60, 0));
        }
    }

    @Override
    public void draw() {

    }

}
