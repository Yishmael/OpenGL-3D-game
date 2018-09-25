package game.inventory.weapons;

import audio.Audio;
import audio.Sound.SoundType;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.entities.objects.Projectile;
import game.entities.units.Player;
import game.inventory.Ammo;
import game.meshes.Texture.TextureType;
import game.meshes.TexturedModel;
import game.time.Timer;
import game.utils.Ray;
import game.utils.RaySystem;

public class Gun extends Weapon {

    protected enum RecoilState {
        NORMAL, RETURN,
    }

    protected RecoilState recoilState;
    protected State state = State.IDLE;

    protected Ammo ammo = new Ammo();

    public Gun(WeaponType type) {
        super(type);
        if (type == WeaponType.PISTOL) {
            model = new TexturedModel(PrimitiveType.PISTOL, TextureType.ROCK, null);
            attackRate = 4f;
            ammo.set(6, 6, 50, 100);
        } else if (type == WeaponType.RIFLE) {
            model = new TexturedModel(PrimitiveType.RIFLE, TextureType.ROCK, null);
            attackRate = 9f;
            ammo.set(30, 30, 200, 200);
        } else if (type == WeaponType.GRENADELAUNCHER) {
            model = new TexturedModel(PrimitiveType.GRENADELAUNCHER, TextureType.ROCK, null);
            attackRate = 11f;
            ammo.set(1, 1, 10, 10);
        }
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public void recoil() {

    }

    @Override
    public void reload() {
        // TODO disable the weapon while reloading
        ammo.reload();
    }

    @Override
    public void startAnimation() {
        state = State.ACTIVE;
        transform.setPosition(initialPosition);
        recoilState = RecoilState.NORMAL;
    }

    @Override
    public void update() {
        float recoilDistance = -0.3f;
        float idleAmp = 1f / 7000;
        float idleSpeed = 2;

        // passive movement
        offset.x = Misc.sin(Timer.now() * idleSpeed) * idleAmp;
        offset.y = Misc.cos(Timer.now() * idleSpeed) * idleAmp;
        offset.z = Misc.cos(Timer.now() * idleSpeed) * idleAmp;

        // recoil
        if (state == State.ACTIVE) {
            if (recoilState == RecoilState.NORMAL) {
                offset.z = Misc.lerp(offset.z, offset.z - recoilDistance, 0.2f);
            } else if (recoilState == RecoilState.RETURN) {
                offset.z = Misc.lerp(offset.z, offset.z + recoilDistance, 0.02f);
            }
        }
        transform.translate(offset);

        if (state == State.ACTIVE) {
            if (recoilState == RecoilState.NORMAL) {
                if (Math.abs(initialPosition.z - recoilDistance - transform.getPosition().z) <= 0.05f) {
                    recoilState = RecoilState.RETURN;
                }
            } else if (recoilState == RecoilState.RETURN) {
                if (Math.abs(initialPosition.z - transform.getPosition().z) <= 0.05f) {
                    state = State.IDLE;
                    transform.setPosition(initialPosition);
                }
            }
        }
    }

    @Override
    public void changeMode() {
        if (type == WeaponType.RIFLE) {
            attackRate = attackRate == 9f ? 4f : 9f;
        }
    }

    @Override
    public void use() {

        if (ammo.isMagazineEmpty()) {
            // play "no ammo" sound
            return;

        }

        if (type == WeaponType.GRENADELAUNCHER) {
            Projectile projectile = new Projectile(PrimitiveType.ICOSPHERE, origin, new Vec3(forward), 0.85f, 0.4f);
            projectile.setTag(tag);
        } else {
            RaySystem.castRay(origin, forward, tag, 1000);
        }
        startAnimation();
        ammo.take(1);
        Audio.getDefaultSource().setPosition(Player.instance.getPosition());
        Audio.getDefaultSource().setPitch(0.9f + Misc.random(0, 0.1f));
        Audio.getDefaultSource().play(SoundType.GUNFIRE);
        lastTimeUsed = Timer.now();

        if (ammo.isMagazineEmpty()) {
            reload();
        }

    }

    @Override
    public String toString() {
        return ammo.toString();
    }

}
