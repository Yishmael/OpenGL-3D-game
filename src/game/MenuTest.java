package game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import game.graphics.Window;
import game.graphics.text.Text;

public class MenuTest {

    Text[] text = new Text[3];
    Window window;

    public static void main(String[] args) {
        new MenuTest().start();
    }

    public MenuTest() {
        window = Window.createInstance(800, 600);
        window.setTitle("Menu Test");
        window.show();

        String[] strings = new String[] { "Play", "Settings", "Exit" };

        for (int i = 0; i < text.length; i++) {
//            text[i] = new Text();
//            text[i].setText(strings[i], new Vector2f(window.getWidth() / 3f, 300 + i * 50), new Vector4f(1, 0, 0, 1),
//                    12);
        }

    }

    public void start() {

        GL11.glClearColor(0, 0.5f, 0.5f, 1);
        while (!window.closed()) {
            update(1 / 60f);
            render();
        }
    }

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        for (Text t: text) {
//            t.draw();
        }

        GLFW.glfwSwapBuffers(window.getWindow());
    }

    public void update(float dt) {

        if (GLFW.glfwGetKey(window.getWindow(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            window.close();
        }
        GLFW.glfwPollEvents();
    }
}
