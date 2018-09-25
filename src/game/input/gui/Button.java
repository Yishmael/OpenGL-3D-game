package game.input.gui;

import org.joml.Vector4f;

import engine.utils.Color;
import engine.utils.Vec2;
import game.CoreCamera;
import game.Misc;
import game.graphics.GUI;
import game.graphics.SceneSystem;
import game.graphics.SceneSystem.Scene;
import game.graphics.Window;
import game.graphics.text.ScreenText;

public class Button {

    public enum ButtonType {
        // instant
        NEW_GAME("New Game"),
        EXIT("Exit"),
        // view change
        PLAY("Play"),
        SETTINGS("Settings"),
        RESOLUTION("Resolution"),
        KEYBOARD_AND_MOUSE("Keyboard & Mouse"),
        BACK("Back"),
        LOAD_GAME("Load Game"),
        // sliders
        FIELD_OF_VIEW("Field of View"),
        SOUND_VOLUME("Sound Volume"),
        MUSIC_VOLUME("Music Volume"),
        // checkboxes
        FULLSCREEN("Fullscreen"),
        VSYNC("Vsync");
        //

        private String text;

        ButtonType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

    }

    // (x, y := top, left)
    private float x, y, width = 0.1f, height = 0.03f;
    private String text = "blank button";
    private boolean active;
    private ButtonType type;
    private Slider slider;
    private Checkbox checkbox;

    public Button(float x, float y, ButtonType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        text = this.type.getText();

        if (type == ButtonType.FIELD_OF_VIEW) {
            slider = new Slider(ButtonType.FIELD_OF_VIEW, this.x + getText().length() / 20f + 0.3f, this.y,
                    CoreCamera.FOV_MIN, CoreCamera.FOV_MAX, CoreCamera.getFov());
        } else if (type == ButtonType.MUSIC_VOLUME) {
            slider = new Slider(ButtonType.MUSIC_VOLUME, this.x + getText().length() / 20f + 0.3f, this.y, 0, 100, 77);
        } else if (type == ButtonType.SOUND_VOLUME) {
            slider = new Slider(ButtonType.SOUND_VOLUME, this.x + getText().length() / 20f + 0.3f, this.y, 0, 100, 77);
        } else if (type == ButtonType.VSYNC) {
            checkbox = new Checkbox(ButtonType.VSYNC, this.x + getText().length() / 20f + 0.3f, this.y, true);
        } else if (type == ButtonType.FULLSCREEN) {
            checkbox = new Checkbox(ButtonType.FULLSCREEN, this.x + getText().length() / 20f + 0.3f, this.y, false);
        }
    }

    public String getText() {
        if (active) {
            return "> " + text + " <";
        } else {
            return text;
        }
    }

    public void trigger() {
        // System.out.println(text);

        switch (type) {
        // instant
        case NEW_GAME:
            SceneSystem.setScene(Scene.GAME);
            break;
        case EXIT:
            Window.instance.close();
            break;
        // change of view
        case RESOLUTION:
            break;
        case SETTINGS:
            GUI.changeView(ButtonType.SETTINGS);
            break;
        case PLAY:
            GUI.changeView(ButtonType.PLAY);
            break;
        case LOAD_GAME:
            break;
        case KEYBOARD_AND_MOUSE:
            break;
        case BACK:
            GUI.back();
            break;
        // sliders
        case FIELD_OF_VIEW:
            break;
        case MUSIC_VOLUME:
            break;
        case SOUND_VOLUME:
            break;

        // checkboxes
        case FULLSCREEN:
            checkbox.toggle();
            break;
        case VSYNC:
            Window.instance.toggleVsync();
            checkbox.toggle();
            break;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean contains(float mouseX, float mouseY) {
        return Misc.isInRange(mouseX, x, x + width) && Misc.isInRange(mouseY, y, y + height);
    }

    public void draw() {
        ScreenText.drawString(getText(), new Vec2(x + 0.15f - getText().length() / 2f / (18 + 2), y), Color.RED, 13);
        if (slider != null) {
            slider.draw();
        }
        if (checkbox != null) {
            checkbox.draw();
        }
    }

    public Slider getSlider() {
        return slider;
    }

}
