package game.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import ai.Event;
import ai.EventSystem;
import ai.Event.EventType;
import engine.utils.Vec3;
import game.Misc;
import game.World;
import game.entities.Entity;
import game.entities.interatives.Interactive;
import game.entities.units.Player;
import game.graphics.GUI;
import game.graphics.HUD;
import game.graphics.SceneSystem;
import game.graphics.SceneSystem.Scene;
import game.graphics.Window;
import game.input.KeyMap.KeyAction;

public abstract class Input {

    private static Vec3 rotDir = new Vec3();
    private static int side, front, air;
    private static boolean attacking;

    public static void init() {
        KeyMap.registerKey(GLFW.GLFW_KEY_W, KeyAction.MOVE_FORWARD);
        KeyMap.registerKey(GLFW.GLFW_KEY_S, KeyAction.MOVE_BACK);
        KeyMap.registerKey(GLFW.GLFW_KEY_A, KeyAction.MOVE_LEFT);
        KeyMap.registerKey(GLFW.GLFW_KEY_D, KeyAction.MOVE_RIGHT);
        KeyMap.registerKey(GLFW.GLFW_KEY_SPACE, KeyAction.JUMP);

        KeyMap.registerKey(GLFW.GLFW_KEY_F, KeyAction.ATTACK);
        KeyMap.registerKey(GLFW.GLFW_KEY_LEFT_ALT, KeyAction.RELOAD);
        KeyMap.registerKey(GLFW.GLFW_KEY_E, KeyAction.USE);

        KeyMap.registerKey(GLFW.GLFW_KEY_G, KeyAction.WEAPON_MODE);

        KeyMap.registerKey(GLFW.GLFW_KEY_KP_ENTER, KeyAction.MENU_CONFIRM);
        KeyMap.registerKey(GLFW.GLFW_KEY_ESCAPE, KeyAction.EXIT);
    }

    public static String getKeyForFunction(KeyAction function) {
        if (function == KeyAction.USE) {
            return "E";
        }
        return "<unbound>";
    }

    public static void handleInput() {
        if (Player.instance == null) {
            return;
        }
        Player.instance.move(new Vec3(side, air, front));

        if (rotDir.lengthSquared() != 0) {
            Player.instance.rotate(rotDir);
        }
        if (attacking) {
            Player.instance.attack();
        }
    }

    public static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {

            // TODO prevent closing via alt-f4
            if (mods == GLFW.GLFW_MOD_ALT && key == GLFW.GLFW_KEY_F4) {

            }

            if (key == GLFW.GLFW_KEY_ESCAPE) {
                Window.instance.close();
            }
            side = 0;
            front = 0;
            air = 0;
            if (GLFW.glfwGetKey(window, KeyMap.getKey(KeyAction.MOVE_FORWARD)) == GLFW_PRESS) {
                front = 1;
            } else if (GLFW.glfwGetKey(window, KeyMap.getKey(KeyAction.MOVE_BACK)) == GLFW_PRESS) {
                front = -1;
            }
            if (GLFW.glfwGetKey(window, KeyMap.getKey(KeyAction.MOVE_LEFT)) == GLFW_PRESS) {
                side = 1;
            } else if (GLFW.glfwGetKey(window, KeyMap.getKey(KeyAction.MOVE_RIGHT)) == GLFW_PRESS) {
                side = -1;
            }

            // debug
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_TAB) == GLFW_PRESS) {
                air = 1;
            } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
                air = -1;
            }

            // temp
            rotDir.reset();
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW_PRESS) {
                rotDir = Misc.getLeftVector();
            } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW_PRESS) {
                rotDir = Misc.getRightVector();
            } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW_PRESS) {
                rotDir = Misc.getDownVector();
            } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW_PRESS) {
                rotDir = Misc.getUpVector();
            }

            if (action == 1 && key == KeyMap.getKey(KeyAction.JUMP)) {
                Player.instance.jump();
            }

            if (key == GLFW.GLFW_KEY_F) {
                if (action == GLFW.GLFW_PRESS) {
                    attacking = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    attacking = false;
                }
            }

            // debug
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_F1) {
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_B) {
                World.showingBoundingBoxes = !World.showingBoundingBoxes;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_KP_0) == GLFW_PRESS) {
                Player.instance.reset();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_R) {

            }
            if (action == GLFW.GLFW_PRESS && key == KeyMap.getKey(KeyAction.RELOAD)) {
                Player.instance.getInventory().getCurrentWeapon().reload();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_T) {
                Player.instance.getInventory().nextWeapon();
            }

            // temp
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                if (key == GLFW.GLFW_KEY_1) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(-0.05f, 0, 0));
                } else if (key == GLFW.GLFW_KEY_2) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(0.05f, 0, 0));
                } else if (key == GLFW.GLFW_KEY_3) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(0, -0.05f, 0));
                } else if (key == GLFW.GLFW_KEY_4) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(0, 0.05f, 0));
                } else if (key == GLFW.GLFW_KEY_5) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(0, 0, -0.05f));
                } else if (key == GLFW.GLFW_KEY_6) {
                    Player.instance.getInventory().getCurrentWeapon().deltaInitialPosition(new Vec3(0, 0, 0.05f));
                }
            }
            // temp
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_KP_5) {
                GUI.up();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_KP_2) {
                GUI.down();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_KP_1) {
                GUI.left();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_KP_3) {
                GUI.right();
            }
            if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_7) {
                SceneSystem.setScene(Scene.MENU);
            }

            if (action == GLFW.GLFW_PRESS && key == KeyMap.getKey(KeyAction.MENU_CONFIRM)) {
                GUI.triggerCurrentButton();
            }
            if (action == GLFW.GLFW_PRESS && key == KeyMap.getKey(KeyAction.WEAPON_MODE)) {
                Player.instance.getInventory().getCurrentWeapon().changeMode();
            }
            if (action == GLFW.GLFW_PRESS && key == KeyMap.getKey(KeyAction.USE)) {
                Entity object = HUD.getInteractiveObject();
                if (object != null) {
                    ((Interactive) object).trigger();
                }
            }

        }

    };

    public static GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            if (yoffset > 0) {
                Player.instance.getInventory().previousWeapon();
            } else {
                Player.instance.getInventory().nextWeapon();
            }
        }
    };

    public static GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                if (action == 1) {
                    double[] x = new double[1];
                    double[] y = new double[1];
                    glfwGetCursorPos(window, x, y);
                    xPrev = Misc.normalizeScreenCoordX(x[0]);
                    yPrev = Misc.normalizeScreenCoordY(y[0]);
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                } else {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                    GLFW.glfwSetCursorPos(window, Window.instance.getWidth() / 2, Window.instance.getHeight() / 2);
                }
            }
        }
    };

    private static double xPrev, yPrev;

    public static GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {

        @Override
        public void invoke(long window, double xPos, double yPos) {
            xPos = Misc.normalizeScreenCoordX(xPos);
            yPos = Misc.normalizeScreenCoordY(yPos);
            float deltaX = (float) (xPos - xPrev);
            float deltaY = (float) (yPos - yPrev);
            if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                Player.instance.rotate(new Vec3(-deltaY * 40, deltaX * 40, 0));
                xPrev = xPos;
                yPrev = yPos;
            }
        }

    };

}
