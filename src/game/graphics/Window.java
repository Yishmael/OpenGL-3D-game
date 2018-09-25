package game.graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import game.input.Input;

public class Window {
    public static Window instance;

    public static Window createInstance(int screenWidth, int screenHeight) {
        if (instance == null) {
            instance = new Window(screenWidth, screenHeight);
            GLFW.glfwSetScrollCallback(instance.getWindow(), Input.scrollCallback);
            GLFW.glfwSetKeyCallback(instance.getWindow(), Input.keyCallback);
            GLFW.glfwSetMouseButtonCallback(instance.getWindow(), Input.mouseButtonCallback);
            GLFW.glfwSetCursorPosCallback(instance.getWindow(), Input.cursorPosCallback);
            return instance;
        } else {
            return instance;
        }
    }

    public static void disableBlending() {
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void disableDepthTest() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void enableBlending() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void enableDepthTest() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private static void setupGraphics() {
        enableDepthTest();
        enableBlending();
        // GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
        // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        // GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private String title = "OpenGL dawn";

    private int width, height;

    private long window;

    private GLFWVidMode vidMode;

    private long monitor;

    int windowCenterX = 0;

    int windowCenterY = 0;

    private boolean vsync;

    private boolean resizable;

    private int majorVersion, minorVersion;
    private boolean forwardCompatible;

    private Window(int width, int height) {
        this.width = width;
        this.height = height;
        if (!GLFW.glfwInit()) {
            System.err.println("GLFW init failed");
        }
        vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        setResizable(false);
        setForwardCompatible(true);
        setVersion(4, 2);
        setFullscreen(false);
        vsync = true;
        setCentered(false);

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, majorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minorVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        window = GLFW.glfwCreateWindow(width, height, title, monitor, 0);
        if (window == 0) {
            System.err.println("Window creation failed!");
        }

        GLFW.glfwSetCursorPos(window, windowCenterX, windowCenterY);
        GLFW.glfwSetWindowPos(window, windowCenterX, windowCenterY);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities(forwardCompatible);

        setupGraphics();

        setViewport(0, 0, width, height);

        GLFW.glfwSwapInterval(vsync ? 1 : 0);
    }

    public void close() {
        GLFW.glfwSetWindowShouldClose(getWindow(), true);
    }

    public boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public int getHeight() {
        return height;
    }

    public float getRatio() {
        return 1f * width / height;
    }

    public int getWidth() {
        return width;
    }

    public long getWindow() {
        return window;
    }

    public void setCentered(boolean centered) {
        if (centered) {
            windowCenterX = (vidMode.width() - width) / 2;
            windowCenterY = (vidMode.height() - height) / 2;
        } else {
            windowCenterX = vidMode.width() / 2;
            windowCenterY = vidMode.height() / 2 * 4 / 5;
        }
    }

    public void setForwardCompatible(boolean forwardCompatible) {
        this.forwardCompatible = forwardCompatible;
    }

    public void setFullscreen(boolean fullscreen) {
        monitor = fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0;
        if (fullscreen) {
            width = vidMode.width();
            height = vidMode.height();
        }
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersion(int major, int minor) {
        this.majorVersion = major;
        this.minorVersion = minor;
    }

    private void setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
        updateSettings();
    }

    public void toggleVsync() {
        setVsync(!vsync);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void show() {
        GLFW.glfwShowWindow(window);
    }

    // TODO update each setting individually
    public void updateSettings() {
        GLFW.glfwSetWindowSize(window, width, height);
        setViewport(0, 0, width, height);

        GLFW.glfwSwapInterval(vsync ? 1 : 0);

        // long newWindow = GLFW.glfwCreateWindow(width, height, title, monitor, window);
        // GLFW.glfwDestroyWindow(window);
        // window = newWindow;
        // GLFW.glfwMakeContextCurrent(window);
        // GL.createCapabilities(forwardCompatible);
        // setupGraphics();

    }
    public static void swapBuffers() {
        GLFW.glfwSwapBuffers(instance.getWindow());
    }

    public static void destroy() {
        GLFW.glfwDestroyWindow(instance.getWindow());
        GLFW.glfwTerminate();
    }

}
