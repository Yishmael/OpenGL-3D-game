package game.entities.units;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import audio.Audio;
import engine.GameObject.PrimitiveType;
import engine.utils.Color;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.entities.Entity;
import game.entities.objects.Projectile;
import game.graphics.Shader;
import game.graphics.text.ScreenText;
import game.inventory.weapons.Gun;
import game.inventory.weapons.Weapon.WeaponType;
import game.meshes.Texture.TextureType;
import game.utils.Ray;

public class Player extends Unit {

    public static Player instance;
    private CoreCamera camera;
    private int score;

    // temp
    public static final float actualHeight = 1.8f;

    // TODO move to engine: regular game object with Input and Camera components
    public Player(Shader shader, float x, float y, float z) {
        super(PrimitiveType.PLAYER, TextureType.WOOD, shader, x, y, z);
        name = "Player";
        instance = this;
        health = new Health(100, 100);

        camera = CoreCamera.createInstance(getPosition(), getRotation());
        inventory.addWeapon(new Gun(WeaponType.PISTOL));
        inventory.addWeapon(new Gun(WeaponType.GRENADELAUNCHER));
    }

    public void addScore() {
        score += 100;
    }

    public CoreCamera getCamera() {
        return camera;
    }

    public int getScore() {
        return score;
    }

    public void jump() {
        if (!flying) {
            verticalSpeed = Misc.JUMP_POWER;
            moved = true;
            flying = true;
        }

    }

    @Override
    public void move(Vec3 offset) {
        super.move(offset);
        // TODO share reference of the transform with the camera
        camera.setPosition(transform.getPosition());
    }

    @Override
    public void onCollision(Entity e) {
        if (Projectile.class.isInstance(e)) {
            if (!((Projectile) e).getTag().equals(name)) {
                int damage = 3;
                health.delta(-damage);
                System.out.println("You took " + damage + " damage");
                if (health.isZero()) {
                    System.out.println("You died.");
                    ScreenText.addTimedText("You died.", new Vec2(-0.5f, 0), Color.RED, 25, 10);
                    destroy(e);
                }
            }
        }
    }

    @Override
    public void prepareForDrawing() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
        super.prepareForDrawing();
    }

    public void reset() {
        transform.set(initialTransform);
        camera.setPosition(transform.getPosition());
        camera.setPosition(transform.getRotation());
        moved = true;
    }

    @Override
    public void rotate(Vec3 rot) {
        super.rotate(new Vec3(rot));
        camera.getTransform().setRotation(transform.getRotation());
    }

    public void setPosition(Vec3 position) {
        transform.setPosition(position);
        camera.setPosition(transform.getPosition());
    }

    @Override
    public void update() {
        Audio.setListenerPosition(transform.getPosition());

        if (flying) {
            verticalSpeed -= Misc.GRAVITY * Misc.dt;
            transform.translate(new Vec3(0, verticalSpeed * Misc.dt, 0));
            camera.setPosition(transform.getPosition());
            if (transform.getPosition().y - actualHeight / 2 <= landingHeight) {
                flying = false;
                transform.setY(landingHeight + actualHeight / 2);
            }
            moved = true;
        }
        super.update();

        health.tick();
    }

}
