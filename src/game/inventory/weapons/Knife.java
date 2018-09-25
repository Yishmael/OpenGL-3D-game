package game.inventory.weapons;

import engine.GameObject.PrimitiveType;
import game.Misc;
import game.World;
import game.meshes.Texture.TextureType;
import game.meshes.TexturedModel;
import game.time.Timer;

public class Knife extends Weapon {

    private enum AttackState {
        NORMAL, RETURN;
    }

    private AttackState attackState;
    private State state;

    public Knife() {
        super(WeaponType.KNIFE);
        model = new TexturedModel(PrimitiveType.KNIFE, TextureType.GRASS, null);
        attackRate = 1.25f;
    }

    @Override
    public void startAnimation() {
        state = State.ACTIVE;
        transform.setPosition(initialPosition);
        attackState = AttackState.NORMAL;
    }

    @Override
    public void update() {
        float stabDistance = 0.85f;
        float idleAmp = 1f / 7000;
        float idleSpeed = 2;

        offset.z = Misc.cos(Timer.now() * idleSpeed) * idleAmp;

        if (state == State.ACTIVE) {
            if (attackState == AttackState.NORMAL) {
                offset.z = Misc.lerp(offset.z, offset.z - stabDistance, 0.35f);
            } else if (attackState == AttackState.RETURN) {
                offset.z = Misc.lerp(offset.z, offset.z + stabDistance, 0.05f);
            }
        }
        transform.translate(offset);

        if (state == State.ACTIVE) {
            if (attackState == AttackState.NORMAL) {
                if (Math.abs(initialPosition.z - stabDistance - transform.getPosition().z) <= 0.05f) {
                    attackState = AttackState.RETURN;
                }
            } else if (attackState == AttackState.RETURN) {
                if (Math.abs(initialPosition.z - transform.getPosition().z) <= 0.05f) {
                    transform.setPosition(initialPosition);
                    state = State.IDLE;
                }
            }
        }
    }

    @Override
    public void use() {
        Cone knifeCone = new Cone(origin, forward, tag, 1);
        World.knifeCone = knifeCone;
        startAnimation();
        // AudioMaster.getDefaultSource().setPitch(0.8f + Misc.random(0, 0.1f));
        // AudioMaster.getDefaultSource().play(SoundType.STAB);
        lastTimeUsed = Timer.now();
    }

    @Override
    public void reload() {
    }

    @Override
    public String toString() {
        return "Knife";
    }

    @Override
    public void changeMode() {

    }

}
