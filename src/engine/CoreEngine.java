package engine;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import engine.GameObject.PrimitiveType;
import engine.physics.Collision;
import engine.physics.PhysicsEngine;
import game.CoreCamera;
import game.graphics.Shader.ShaderType;
import game.graphics.Skybox;
import game.graphics.Skybox.SkyboxType;
import game.graphics.Window;
import game.meshes.Texture.TextureType;
import game.time.FrameCounter;
import game.utils.Loader;

public class CoreEngine {

    private float dt = 1 / 60f;
    // TODO put it back to private
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Window window;

    public CoreEngine(int width, int height) {
        window = Window.createInstance(width, height);
        window.setCentered(false);
        window.setVsync(!FrameCounter.isShown());
        window.setFullscreen(false);
        window.show();        
        
        //temp
        CoreCamera.createInstance();
        
        preloadPrimitives();
        preloadShaders();
        preloadTextures();
        Prefabs.init();
        
        Skybox.init();

        gameObjects.add(Prefabs.createPlane());
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
    }

    public void updateAll(float dt) {
        GLFW.glfwPollEvents();

        for (Iterator<GameObject> iter = gameObjects.iterator(); iter.hasNext();) {
            GameObject go = iter.next();
            go.update(dt);
            if (go.isDestroyed()) {
                iter.remove();
            }
        }

        PhysicsEngine.simulateAll(gameObjects, dt);
        Collision.detectCollisions(gameObjects);

        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            window.close();
        }
        {
            int err;
            while ((err = GL11.glGetError()) != GL11.GL_NO_ERROR) {
                System.out.println(err);
            }
        }
        FrameCounter.tick();
    }

    public void drawAll() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Skybox.draw();

        for (GameObject go: gameObjects) {
            go.draw();
        }

        glfwSwapBuffers(window.getWindow());
    }

    public void run() {
        GL11.glClearColor(0.7f, 0.5f, 0.5f, 1);
        while (!window.closed()) {
            updateAll(dt);
            drawAll();
        }
        GLFW.glfwDestroyWindow(window.getWindow());
        GLFW.glfwTerminate();
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void run(int numUpdates) throws InterruptedException {
        float totalTime = 0;
        GL11.glClearColor(0.7f, 0.5f, 0.5f, 1);
        for (int i = 0; i < numUpdates; i++) {
            while (totalTime < 300 / 1000f) {
                updateAll(dt);
                totalTime += dt;
            }
            totalTime = 0;
            drawAll();
            Thread.sleep(300);
            for (GameObject gameObject: gameObjects) {
                // System.out.println(gameObject);
                break;
            }
        }
        while (!window.closed()) {
            glfwPollEvents();
            // nop nop nop
        }
    }

    private void preloadPrimitives() {
        for (PrimitiveType prim: PrimitiveType.values()) {
            Loader.loadMesh(prim);
        }

    }

    private void preloadShaders() {
        for (ShaderType shader: ShaderType.values()) {
            Loader.loadShader(shader);
        }

    }

    private void preloadTextures() {
        for (TextureType texture: TextureType.values()) {
            Loader.loadTexture(texture);
        }

    }

}
