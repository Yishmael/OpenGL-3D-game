package game.input;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

public abstract class KeyMap {

    // TODO allow for key->action and action->key mapping

    private static HashMap<KeyAction, Integer> actionToKey = new HashMap<>();
    private static HashMap<Integer, KeyAction> keyToAction = new HashMap<>();

    public enum KeyAction {
        MOVE_FORWARD("Move forward"),
        MOVE_BACK("Move back"),
        MOVE_LEFT("Strafe left"),
        MOVE_RIGHT("Strafe right"),
        JUMP("Jump"),

        USE("Use"),
        ATTACK("Shoot"),
        RELOAD("Reload"),

        WEAPON_MODE("Change fire mode"),
        // WEAPON_PREV,
        // WEAPON_NEXT,

        MENU_CONFIRM("Menu confirm"),
        EXIT("Exit");

        private String text;

        KeyAction(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static void registerKey(int key, KeyAction action) {
        if (!keyToAction.containsKey(key)) {
            keyToAction.put(key, action);
            actionToKey.put(action, key);
        } else {
            System.out.println("Warning: " + keyToAction.get(key).getText() + " key cleared.");
            keyToAction.put(key, action);
            actionToKey.put(action, key);
        }
    }

    public static int getKey(KeyAction action) {
        if (!actionToKey.containsKey(action)) {
            return 0;
        }
        return actionToKey.get(action);

    }

    public static void printMapping() {
        for (Map.Entry<Integer, KeyAction> entry: keyToAction.entrySet()) {
            int key = entry.getKey();
            String text = entry.getValue().getText();
            System.out.println(text + " : " + getKeyName(key));
        }
    }

    public static String getKeyName(int key) {
        String name = "<UNKNOWN>";

        switch (key) {
        case GLFW.GLFW_KEY_W:
            name = "W";
            break;
        case GLFW.GLFW_KEY_S:
            name = "S";
            break;
        case GLFW.GLFW_KEY_A:
            name = "A";
            break;
        case GLFW.GLFW_KEY_D:
            name = "D";
            break;
        case GLFW.GLFW_KEY_SPACE:
            name = "Space";
            break;
        case GLFW.GLFW_KEY_LEFT_ALT:
            name = "Left Alt";
            break;
        case GLFW.GLFW_KEY_E:
            name = "E";
            break;
        case GLFW.GLFW_KEY_F:
            name = "F";
            break;
        case GLFW.GLFW_KEY_G:
            name = "G";
            break;
        case GLFW.GLFW_KEY_KP_ENTER:
            name = "Numpad Enter";
            break;
        case GLFW.GLFW_KEY_ESCAPE:
            name = "Esc";
            break;

        }

        return name;
    }
}
