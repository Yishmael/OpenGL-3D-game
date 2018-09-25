package engine.game;

import engine.CoreEngine;
import engine.GameObject;

public abstract class Game {
    CoreEngine coreEngine;

    protected Game(int screenWidth, int screenHeight) {
        coreEngine = new CoreEngine(screenWidth, screenHeight);
    }

    protected void start() throws InterruptedException {
        coreEngine.run();
    }
    
    protected void addObject(GameObject go) {
        coreEngine.addGameObject(go);
    }
}
