package game.time;

public abstract class FrameCounter {
    private static float lastTime;
    private static int fps;
    private static boolean shown = false;

    public static void tick() {

        if (Timer.now() - lastTime > 1) {
            fps++;
            if (shown) {
                System.out.println("FPS: " + fps);
            }
            fps = 0;
            lastTime = Timer.now();
        } else {
            fps++;
        }
    }

    public static boolean isShown() {
        return shown;
    }
}
