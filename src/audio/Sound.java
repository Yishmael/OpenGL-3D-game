package audio;

public class Sound {
    public enum SoundType {
        GUNFIRE, TELEPORT, EXPLOSION, IMPACT, BACKGROUND_MUSIC,
    }

    private int buffer;
    private SoundType type;

    protected Sound(SoundType type, int buffer) {
        this.type = type;
        this.buffer = buffer;
    } 

    protected int getBuffer() {
        return buffer;
    }

    protected SoundType getType() {
        return type;
    }
}
