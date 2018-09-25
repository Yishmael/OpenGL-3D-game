package engine.scripts;

import engine.GameObject;

public class ScriptEngine {
    /* enqueue scripts
     * execute scripts on a game object
     */
    public enum Command {
        KILL;
    }

    public static final void applyCommand(GameObject go, Command cmd) {
        switch (cmd) {
        case KILL:
            go.destroy();
            break;
        }
    }
}
