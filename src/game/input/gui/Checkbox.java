package game.input.gui;

import org.joml.Vector4f;

import engine.utils.Color;
import engine.utils.Vec2;
import game.graphics.text.ScreenText;
import game.input.gui.Button.ButtonType;

public class Checkbox {

    // (x, y := top, left)
    private float x, y, width = 0.02f, height = 0.02f;
    private boolean checked;
    private ButtonType buttonType;

    public Checkbox(ButtonType buttonType, float x, float y, boolean checked) {
        this.buttonType = buttonType;
        this.x = x;
        this.y = y;
        this.checked = checked;
    }

    public void toggle() {
        checked = !checked;
    }

    public void draw() {
        ScreenText.drawString(checked ? "[x]" : "[ ]", new Vec2(x, y), Color.CYAN, 10);
    }

}
