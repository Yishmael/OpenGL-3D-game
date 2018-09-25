package game;

import org.lwjgl.glfw.GLFW;
import org.omg.CORBA.VisibilityHelper;

import ai.EventSystem;
import ai.VisibilitySystem;
import audio.Audio;
import game.graphics.HUD;
import game.graphics.MasterRenderer;
import game.graphics.SceneSystem;
import game.graphics.ShaderSystem;
import game.graphics.Skybox;
import game.graphics.Window;
import game.graphics.particles.ParticleSystem;
import game.graphics.text.Text;
import game.input.Input;
import game.time.FrameCounter;
import game.utils.RaySystem;

public class Game {
    // private Terrain terrain;

    private void draw() {
        Skybox.draw();
        SceneSystem.draw();
        RaySystem.draw();
        // terrain.draw();

        Window.swapBuffers();
    }

    private void start() {
        Window window = Window.createInstance(800, 600);
        window.setCentered(false);
        window.setVsync(!FrameCounter.isShown());
        window.show();

        ShaderSystem.init();
        Skybox.init();
        Audio.init();
        HUD.init();
        Text.init();
        Input.init();
        ParticleSystem.init();
        SceneSystem.init();

        // terrain = new Terrain();

        MasterRenderer.setClearColor(0, 0.5f, 0.5f, 1);
        while (!window.closed()) {
            GLFW.glfwPollEvents();
            update();
            MasterRenderer.clear();
            draw();
        }
        Window.destroy();
        Audio.delete();
    }

    private void update() {
        Input.handleInput();
        FrameCounter.tick();
        SceneSystem.update();
        EventSystem.update();
        VisibilitySystem.update();
        RaySystem.update();
        // listeners.add(c -> System.out.println(c.message()));
    }

    public static void main(String[] args) {
        new Game().start();
    }

}
