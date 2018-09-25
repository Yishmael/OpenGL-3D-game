package game.entities.units;

import ai.Agent;
import ai.Patrol;
import ai.Agent.State;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.World;
import game.entities.DynamicObject;
import game.graphics.Shader;
import game.inventory.Inventory;
import game.inventory.weapons.Gun;
import game.inventory.weapons.Weapon;
import game.inventory.weapons.Weapon.WeaponType;
import game.meshes.Texture.TextureType;

public class Unit extends DynamicObject {

    protected Agent agent;
    protected Health health = new Health(30, 30);
    protected Inventory inventory = new Inventory();

    public Unit(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z) {
        this(prType, texType, shader, x, y, z, 0, 0, 0, 1);
    }

    public Unit(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z, float rx, float ry,
            float rz, float scale) {
        super(prType, texType, shader, x, y, z, rx, ry, rz, scale);
        name = prType.name() + " " + World.units.size();

        if (prType != PrimitiveType.PLAYER) {
            agent = new Agent(this);
        }
        
    }

    public void attack() {
        if (inventory.getCurrentWeapon().isReady()) {

            Vec3 origin = new Vec3(getPosition());
            Vec3 forward = Misc.getForwardVector(getRotation());
            inventory.getCurrentWeapon().setData(name, origin, forward);
            inventory.getCurrentWeapon().use();
        }
    }

    public Agent getAgent() {
        return agent;
    }

    public Health getHealth() {
        return health;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void update() {
        if (agent != null) {
            agent.update();
        }

        if (inventory != null) {
            inventory.updateCurrentWeapon();
        }

        super.update();
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

}
