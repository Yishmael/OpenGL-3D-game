package engine.scripts;

import audio.Audio;
import audio.Sound.SoundType;
import engine.utils.Vec3;
import game.entities.Entity;
import game.time.Timer;

public class Teleporter {
    Entity entity;
    float time, initTime;
    Vec3 destination;

    public Teleporter(Entity entity, Vec3 destination, float time) {
        this.entity = entity;
        this.destination = new Vec3(destination);
        this.time = time;
        initTime = Timer.now();
    }

    public boolean isReady() {
        return Timer.now() - initTime >= time;
    }

    public void update() {

    }

    public void execute() {
        Audio.getDefaultSource().play(SoundType.TELEPORT);
        entity.getPosition().x = destination.x;
        entity.getPosition().y = destination.y;
        entity.getPosition().z = destination.z;
    }
}
