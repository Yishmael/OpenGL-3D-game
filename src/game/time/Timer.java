package game.time;

import org.lwjgl.glfw.GLFW;

public class Timer {
    public static float now() {
        return (float) GLFW.glfwGetTime();
    }

    public static void showTime() {
        int seconds = 0;
        int minutes = 0;
        float time = (float) GLFW.glfwGetTime();

        while (time >= 60) {
            minutes++;
            time -= 60;
        }
        seconds = (int) time;
        System.out.println((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
    }

    private static float t;

    public static void start() {
        t = now();
    }

    public static void stop() {
        int ratio = (int) ((now() - t) * 60f * 100f);
        System.out.println(ratio + " %");
    }
}
