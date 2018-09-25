package game.entities.interatives;

import engine.GameObject.PrimitiveType;
import game.entities.Entity;
import game.entities.StaticObject;
import game.graphics.Shader;
import game.input.Input;
import game.input.KeyMap.KeyAction;
import game.meshes.Texture.TextureType;

public class Ladder extends StaticObject implements Interactive {
    private boolean active;

    public Ladder(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z, float rx,
            float ry, float rz, float scale) {
        super(prType, texType, shader, x, y, z, rx, ry, rz, scale);

        name = "Ladder";
    }

    @Override
    public void trigger() {
        active = !active;
        
        System.out.println("Using ladder: " + active);

        if (active) {
            // TODO change player's movement mode to "ladder"
            // W -> up, S -> down, A and D have no effect
        } else {
            // change back to normal movement
        }
    }

    @Override
    public void onCollision(Entity e) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getMessage() {
        return "Press " + Input.getKeyForFunction(KeyAction.USE) + " to climb";
    }

}
