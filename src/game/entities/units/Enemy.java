package game.entities.units;

import ai.Agent;
import ai.Agent.State;
import ai.Patrol;
import engine.GameObject;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.entities.Entity;
import game.entities.objects.Projectile;
import game.graphics.Shader;
import game.inventory.weapons.Gun;
import game.inventory.weapons.Weapon;
import game.inventory.weapons.Weapon.WeaponType;
import game.meshes.Texture.TextureType;
import game.time.Timer;
import game.utils.Ray;
import game.utils.RaySystem;

public class Enemy extends Unit {
    private float lastTimeVisionUpdated;

    public Enemy(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z) {
        super(prType, texType, shader, x, y, z);
        
        // TODO allow attacking without requiring an inventory and a weapon
        Weapon weapon = new Gun(WeaponType.GRENADELAUNCHER);
        weapon.setAttackRate(1f);
        ((Gun) weapon).getAmmo().set(10000, 0, 0, 0);;
        getInventory().addWeapon(weapon);
        
        speed = 0.002f;
        patrol = new Patrol(transform);
        patrol.addPosition(new Vec3(3, 0, 0));
        patrol.addPosition(new Vec3(0, 0, -4));
    }

    @Override
    public void update() {
         Vec3 diff = Player.instance.getPosition().sub(getPosition()).normalize();
         float dx = diff.x;
         float dz = diff.z;
         float extraRotation = dx < 0 && dz < 0 ? -Misc.PI / 2 : Misc.PI / 2;
         extraRotation = dx < 0 && dz > 0 ? -extraRotation : extraRotation;
         getTransform().setRotation(new Vec3(0, extraRotation + Misc.atan(dz / dx), 0));

         getTransform().setRotation(getTransform().getRotation().add(new Vec3(0, 0.01f, 0)));

        float minDistance = 5;
        if (getPosition().distanceTo(Player.instance.getPosition()) <= minDistance) {
            if (agent != null) {
                if (agent.getState() == State.ATTACKING) {
                    attack();
                }
            }
        }

        if (Timer.now() - lastTimeVisionUpdated > 0.3f) {
            lastTimeVisionUpdated = Timer.now();

            RaySystem.castRay(getPosition(), Misc.getDirection(getPosition(), Player.instance.getPosition()), name, 10,
                    0);
            
            Ray ray = RaySystem.rays.get(RaySystem.rays.size() - 1);
            if (ray.reaches(Player.instance)) {
                System.out.println(name + " sees the player");
            }

        }
        super.update();
    }
    
    @Override
    public void onCollision(Entity e) {
        if (Projectile.class.isInstance(e)) {
            if (!((Projectile) e).getTag().equals(name)) {
                health.delta(-10);
                System.out.println(name + " took 10 damage");
                if (health.isZero()) {
                    destroy(e);
                    ((Projectile) e).destroy(this);
                }
            }
        }
    }

}
