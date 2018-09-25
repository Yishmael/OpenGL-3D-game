package game.entities.objects.powerups;

import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.entities.DynamicObject;
import game.entities.Entity;
import game.entities.behaviours.Rotator;
import game.entities.behaviours.Translator;
import game.entities.units.Player;
import game.entities.units.Unit;
import game.graphics.Shader;
import game.inventory.weapons.Gun;
import game.inventory.weapons.Knife;
import game.inventory.weapons.Weapon.WeaponType;
import game.meshes.Texture.TextureType;

public class LootableItem extends DynamicObject {
    public enum LootableType {
        AMMO, HEALTHPACK, KNIFE, RIFLE;
    }

    private Rotator rotator;
    private Translator translator;
    private LootableType type;

    public LootableItem(LootableType type, PrimitiveType prType, Shader shader, float x, float y, float z) {
        super(prType, TextureType.STONE, shader, x, y, z);
        this.type = type;

        translator = new Translator(transform, Misc.getUpVector(), 0.3f, true);
        rotator = new Rotator(transform, new Vec3(0, 1, 0), Misc.toRadians(180000), true);
    }

    public void getBonus(Unit unit) {
        switch (type) {
        case RIFLE:
            unit.getInventory().addWeapon(new Gun(WeaponType.RIFLE));
            System.out.println("Picked up " + name);
            break;
        case KNIFE:
            unit.getInventory().addWeapon(new Knife());
            System.out.println("Picked up " + name);
            break;
        case AMMO:
            int amount = 100;
            if (unit.getName().startsWith("Player")) {
                if (Gun.class.isAssignableFrom(Player.instance.getInventory().getCurrentWeapon().getClass())) {
                    ((Gun) unit.getInventory().getCurrentWeapon()).getAmmo().put(amount);
                    System.out.println("Picked up " + amount + " ammo");
                }
            }
            break;
        case HEALTHPACK:
            int h = 20;
            unit.getHealth().delta(h);
            System.out.println("Health +" + h);
            break;
        default:
            System.err.println("Unknown lootable type " + type);
            break;
        }
    }

    @Override
    public void update() {
        translator.update();
        rotator.update();

        super.update();
    }

    @Override
    public void onCollision(Entity e) {
        if (Unit.class.isAssignableFrom(e.getClass())) {
            getBonus((Unit) e);
            destroy(e);
        }
    }

}
