package game.input.gui;

import audio.Audio;
import audio.Sound.SoundType;
import audio.Source;
import engine.utils.Color;
import engine.utils.Vec2;
import game.CoreCamera;
import game.Misc;
import game.graphics.text.ScreenText;
import game.input.gui.Button.ButtonType;

public class Slider {

    // (x, y := top, left)
    private float x, y, width = 0.1f, height = 0.02f;
    private int min, max, value;
    private String text = "";
    private ButtonType buttonType;

    public Slider(ButtonType buttonType, float x, float y, int min, int max, int defaultValue) {
        this.buttonType = buttonType;
        this.x = x;
        this.y = y;
        this.min = min;
        this.max = max;

        setValue(defaultValue);
        update();
    }

    public Slider(ButtonType buttonType, float x, float y, int min, int max) {
        this(buttonType, x, y, min, max, min);
    }

    public void deltaValue(int delta) {
        if (delta == 0) {
            return;
        }
        value = Misc.getClamped(value + delta, min, max);
        update();
    }

    private void update() {
        switch (buttonType) {
        case FIELD_OF_VIEW:
            CoreCamera.setFov(value);
            break;
        case SOUND_VOLUME:
            Audio.setSoundVolume(value);
            new Source().play(SoundType.EXPLOSION);
            break;
        case MUSIC_VOLUME:
            Audio.setMusicVolume(value);
            break;
        default:
            break;
        }

        text = "";
        int dotCount = 10 - (Misc.digitCount(value) - 1);
        for (int i = 0; i < dotCount + 1; i++) {
            if (Misc.isInRange((float) (value - min) / (max - min) * dotCount, i, i + 1 - 0.0001f)) {
                text += value;
                // text += "#";
            } else {
                text += ".";
            }
        }
        text = min + " " + text + " " + max;
    }

    public boolean contains(float mouseX, float mouseY) {
        return Misc.isInRange(mouseX, x, x + width) && Misc.isInRange(mouseY, y, y + height);
    }

    public void draw() {
        ScreenText.drawString(text, new Vec2(x - text.length() / 2f / (18 + 2), y), Color.CYAN, 10);
    }

    public void setValue(int value) {
        this.value = Misc.getClamped(value, min, max);
        update();
    }

    public int getValue() {
        return value;
    }

}
