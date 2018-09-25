package game.graphics.text;

import org.joml.Vector4f;

import engine.utils.Color;
import engine.utils.Vec2;
import game.time.Timer;

public class ScreenTextData {
    private String text;
    private Vec2 position;
    private Color color;
    private float size;
    private float duration;
    private float creationTime;

    public ScreenTextData(String text, Vec2 position, Color color, float size, float duration) {
        this.text = text;
        this.position = new Vec2(position);
        this.color = new Color(color);
        this.size = size;
        this.duration = duration;

        creationTime = Timer.now();
    }

    public Color getColor() {
        return color;
    }

    public Vec2 getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }

    public String getText() {
        return text;
    }

    public float getDuration() {
        return duration;
    }

    public float getCreationTime() {
        return creationTime;
    }
}
