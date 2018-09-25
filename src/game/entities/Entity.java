package game.entities;

import java.util.ArrayList;

import ai.Agent;
import engine.GameObject.PrimitiveType;
import engine.utils.Vec3;
import game.Misc;
import game.World;
import game.entities.units.Health;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.particles.ParticleInfo;
import game.graphics.particles.ParticleSystem;
import game.graphics.particles.ParticleType;
import game.inventory.Inventory;
import game.meshes.Texture.TextureType;
import game.meshes.TexturedModel;
import game.utils.AABB;
import game.utils.Transform;

public abstract class Entity {
    protected String name;
    protected Shader shader;
    protected TexturedModel model;
    protected Transform initialTransform, transform;

    protected float landingHeight = Misc.BOTTOM_ELEVATION;

    protected ArrayList<AABB> boundingBoxes = new ArrayList<>();

    public Entity(PrimitiveType prType, TextureType texType, Shader shader) {
        this(prType, texType, shader, 0, 0, 0, 0, 0, 0, 1);
    }

    public Entity(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z) {
        this(prType, texType, shader, x, y, z, 0, 0, 0, 1);
    }

    public Entity(PrimitiveType prType, TextureType texType, Shader shader, float x, float y, float z, float rx,
            float ry, float rz, float scale) {
        model = new TexturedModel(prType, texType, shader);
        this.shader = shader;
        this.transform = new Transform(new Vec3(x, y, z), new Vec3(rx, ry, rz), scale);
        initialTransform = new Transform(this.transform);

        for (int i = 0; i < model.getMesh().getModelCount(); i++) {
            Vec3 min = new Vec3(model.getMesh().getMinExtends(i));
            Vec3 max = new Vec3(model.getMesh().getMaxExtends(i));

            boundingBoxes.add(new AABB(name, transform, min, max));
        }

        name = prType + " " + World.units.size() + World.dynamicObjects.size() + World.staticObjects.size();

    }

    public ArrayList<AABB> getBoundingBoxes() {
        return boundingBoxes;
    }

    public float getLandingHeight() {
        return landingHeight;
    }

    public TexturedModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public Vec3 getPosition() {
        return transform.getPosition();
    }

    public Vec3 getRotation() {
        return transform.getRotation();
    }

    public float getScale() {
        return transform.getScale();
    }

    public Shader getShader() {
        return shader;
    }

    public Transform getTransform() {
        return transform;
    }

    public void onCollision(Entity e) {

    }

    public void prepareForDrawing() {
        model.getMesh().bind();
        model.getTexture().bind();
        model.getMesh().getShader().uploadMatrix4f("u_mMatrix", transform.getMatrix());
        model.getMesh().getShader().uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
    }

    public void setLandingHeight(float landHeight) {
        this.landingHeight = landHeight;
    }

    @Override
    public String toString() {
        return name + " " + transform.getPosition();
    }

    public void update() {

        for (AABB bb: boundingBoxes) {
            // bb.updatePosition(getPosition());

            // temp
            bb.setName(name + "'s BB");
        }

    }

}
