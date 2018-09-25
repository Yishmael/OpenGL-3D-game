package engine.scripts;

import engine.GameObject;
import engine.scripts.ScriptEngine.Command;

public class CollisionResponse {

    private String objectOfInterest;
    private Command cmd;

    public CollisionResponse(String objectOfInterest, Command cmd) {
        this.objectOfInterest = objectOfInterest;
        this.cmd = cmd;
    }

    public boolean triggersAt(String name) {
        return name.equals(objectOfInterest);
    }

    public void execute(GameObject go) {
        ScriptEngine.applyCommand(go, cmd);
    }

    public String getObjectOfInterest() {
        return objectOfInterest;
    }
}
