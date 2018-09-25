package game.graphics;

import java.util.ArrayList;

import game.input.gui.Button;
import game.input.gui.Button.ButtonType;

// TODO combine Button with GUI classes
public class GUI {

    private static GUI current;
    private Button button;
    private ButtonType type;
    private GUI parent;
    private ArrayList<GUI> children = new ArrayList<>();

    public GUI(ButtonType type) {
        this.type = type;
    }

    public static void draw() {
        for (int i = 0; i < current.children.size(); i++) {
            GUI child = current.children.get(i);
            child.button.draw();
        }
    }

    public static GUI getCurrent() {
        return current;
    }

    public static void init() {
        GUI root = new GUI(null);

        GUI play = new GUI(ButtonType.PLAY);
        play.addChild(new GUI(ButtonType.NEW_GAME));
        play.addChild(new GUI(ButtonType.LOAD_GAME));
        play.addChild(new GUI(ButtonType.BACK));
        root.addChild(play);

        GUI settings = new GUI(ButtonType.SETTINGS);
        settings.addChild(new GUI(ButtonType.VSYNC));
        settings.addChild(new GUI(ButtonType.RESOLUTION));
        settings.addChild(new GUI(ButtonType.FIELD_OF_VIEW));
        settings.addChild(new GUI(ButtonType.FULLSCREEN));
        settings.addChild(new GUI(ButtonType.SOUND_VOLUME));
        settings.addChild(new GUI(ButtonType.MUSIC_VOLUME));
        GUI keys = new GUI(ButtonType.KEYBOARD_AND_MOUSE);
        settings.addChild(keys);
        settings.addChild(new GUI(ButtonType.BACK));
        root.addChild(settings);

        root.addChild(new GUI(ButtonType.EXIT));

        current = root;
        play.setActive(true);
    }

    public static void changeView(ButtonType type) {
        for (GUI child: current.children) {
            if (child.type == type) {
                child.button.setActive(false);
                current = child;
                current.children.get(0).button.setActive(true);
                return;
            }
        }
    }

    public static void back() {
        for (GUI child: current.children) {
            child.button.setActive(false);
        }
        if (current.parent != null) {
            current = current.parent;
            current.children.get(0).button.setActive(true);
        }
    }

    private void setActive(boolean active) {
        button.setActive(active);
    }

    public static void triggerCurrentButton() {
        for (GUI child: current.children) {
            if (child.button.isActive()) {
                child.button.trigger();
                return;
            }
        }
    }

    // TODO merge "down" and "up" methods
    public static void down() {
        for (int i = 0; i < current.children.size(); i++) {
            GUI child = current.children.get(i);
            if (child.button.isActive()) {
                child.button.setActive(false);
                i = (i + 1) % current.children.size();
                current.children.get(i).button.setActive(true);
                return;
            }
        }
    }

    public static void up() {
        for (int i = 0; i < current.children.size(); i++) {
            GUI child = current.children.get(i);
            if (child.button.isActive()) {
                child.button.setActive(false);
                i = i == 0 ? current.children.size() - 1 : i - 1;
                current.children.get(i).button.setActive(true);
                return;
            }
        }
    }

    public static void printMenuTree() {
        for (int i = 0; i < current.getDepth() - 1; i++) {
            System.out.print("--");
        }

        for (GUI child: current.children) {
            System.out.println(child.type);
        }

        if (current.children.isEmpty()) {
            System.out.println("<Empty>");
        }
    }

    public void addChild(GUI child) {
        child.parent = this;
        child.button = new Button(-0.5f, 0.5f - 0.1f * children.size(), child.type);
        children.add(child);
    }

    public int getDepth() {
        if (parent == null) {
            return 0;
        }
        return parent.getDepth() + 1;
    }

    public static void left() {
        sliderDelta(-5);
    }

    public static void right() {
        sliderDelta(5);
    }

    private static void sliderDelta(int delta) {
        for (GUI child: current.children) {
            if (child.button.isActive()) {
                if (child.button.getSlider() != null) {
                    child.button.getSlider().deltaValue(delta);
                }
            }
        }

    }
}
