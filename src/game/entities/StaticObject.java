package game.entities;

import engine.GameObject.PrimitiveType;
import game.World;
import game.graphics.Shader;
import game.meshes.Texture.TextureType;

public class StaticObject extends Entity {
    // immovable objects: only interact with other objects via collision

    public StaticObject(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z) {
        this(prType, texType, shader, x, y, z, 0, 0, 0, 1);
    }
    
    public StaticObject(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z, float rx,
            float ry, float rz, float scale) {
        super(prType, texType, shader, x, y, z, rx, ry, rz, scale);
        name = prType.name() + " " + World.staticObjects.size();
    }

    @Override
    public void onCollision(Entity e) {
    }

    @Override
    public void update() {
        
        super.update();
    }
}
