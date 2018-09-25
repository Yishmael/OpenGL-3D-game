package game.graphics;

import engine.utils.Color;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.World;
import game.entities.units.Player;
import game.graphics.particles.ParticleInfo;
import game.graphics.particles.ParticleSystem;
import game.graphics.particles.ParticleType;
import game.graphics.text.ScreenText;
import game.graphics.text.WorldText;
import game.input.Input;
import game.utils.Collision;

public abstract class SceneSystem {

    public enum Scene {
        MENU, GAME,
    }

    private static Scene current = Scene.GAME;

    public static void setScene(Scene scene) {
        if (current != scene) {
            current = scene;
            init();
        }
    }

    public static void init() {
        if (current == Scene.MENU) {
            GUI.init();
        } else if (current == Scene.GAME) {
            ParticleSystem.addParticle(ParticleType.FIRE, ParticleInfo.FLAME, new Vec3(0, 0, 2));
            World.addUnit(new Player(null, 0, 1.8f / 2, 5));
            World.init();
        }
    }

    public static void update() {
        if (current == Scene.MENU) {

        } else if (current == Scene.GAME) {
            Collision.update();
            ParticleSystem.update();
            World.update();
        }
    }

    public static void draw() {
        if (current == Scene.MENU) {
            GUI.draw();
        } else if (current == Scene.GAME) {
            MasterRenderer.draw();
            Player player = Player.instance;
            ScreenText.drawString("Score:" + player.getScore(), new Vec2(-0.9f, 0.9f), Color.BLUE, 12f);
            ScreenText.drawString("Pos: " + player.getPosition(), new Vec2(-0.9f, 0.75f), Color.PINK, 7f);

            ScreenText.drawString("" + player.getInventory().getCurrentWeapon(), new Vec2(-0.9f, -0.8f),
                    new Color(0, 0.4f, 0), 8f);

            ScreenText.drawString("" + player.getHealth(), new Vec2(0.7f, -0.8f), new Color(0, 0.4f, 0), 8f);
            WorldText.drawString("3D text..", new Vec3(0, 3, 0), new Color(1.7f, 0.7f, 0.7f), 3f);
        }

    }
}
