package game.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.World;
import game.data.Resources;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.Entity;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.graphics.particles.ParticleSystem;
import game.time.Timer;

public class Ray {
    private float duration;
    private Vec3 origin, direction;
    private String tag;
    private float range;
    private float[] positions;
    private VertexArray vao;
    private Shader shader;
    private float creationTime;
    private ArrayList<Entity> targets = new ArrayList<>();
    private Entity closestTarget;

    private boolean destroyed;

    // TODO allocate data on the GPU once for all rays
    // TODO fix angle in some quadrants
    protected Ray(Vec3 origin, Vec3 direction, String tag, float range, float duration) {
        this.origin = origin;
        this.direction = direction.normalize();
        this.tag = tag;
        this.range = range;
        this.duration = duration;

        Vec3 end = this.origin.add(direction.mul(range));

        positions = new float[] { //
                origin.x, origin.y, origin.z, //
                end.x, end.y, end.z, //
        };

        shader = Resources.shaders.get(ShaderType.MISC);

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();

        shader.bind();
        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());

        creationTime = Timer.now();

        ArrayList<Entity> objects = new ArrayList<>();
        objects.addAll(World.units);
        objects.addAll(World.dynamicObjects);
        objects.addAll(World.staticObjects);
        for (Entity obj: objects) {
            if (reaches(obj)) {
                targets.add(obj);
            }
        }

        if (targets.size() > 0) {
            // System.out.println("Target count: " + targets.size());
            closestTarget = targets.get(0);
            for (Entity t: targets) {
                if (origin.distanceTo(t.getPosition()) < origin.distanceTo(closestTarget.getPosition())) {
                    closestTarget = t;
                }
            }
            // System.out.println("Closest target: " + closestTarget.getName());
        }
    }

    public void draw() {
        shader.bind();
        vao.bind();
        shader.uploadVector3f("u_color", new Vec3(1, 0, 0));
        shader.uploadMatrix4f("u_mMatrix", new Matrix4f());
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
        vao.unbind();
        shader.unbind();
    }

    public float getCreationTime() {
        return creationTime;
    }

    public String getTag() {
        return tag;
    }

    public boolean hasExpired() {
        return Timer.now() - creationTime > duration;
    }

    public boolean isDestroyed() {
        return destroyed || hasExpired();
    }

    // TODO fix for intersection between a bounding box
    public boolean reaches(Entity entity) {
        Vec3 point = entity.getTransform().getPosition();

        Vec3 distance = point.sub(origin);
        Vec3 normalizedDistance = distance.normalized();

        float cos = normalizedDistance.dot(direction);
        if (cos < 0) {
            // going the opposite direction
            return false;
        }

        if (distance.length() > range) {
            // System.out.println("Out of range: d=" + distance.length());
            return false;
        }

        float alpha = (float) Math.acos(cos);

        Vec3 minimumDistance = distance.mul((float) Math.tan(alpha));

        float absDistance = minimumDistance.length();

        return absDistance < 0.5f;
    }

    public void destroy() {
        destroyed = true;
    }

    public void triggerAt(Entity target) {
        ParticleSystem.addImpactParticle(target.getModel().getTextureType(), target.getPosition());
        destroy();
    }

    public float getDuration() {
        return duration;
    }

    public boolean damages(Entity entity) {
        return !tag.equals(entity.getName());
    }

    public ArrayList<Entity> getTargets() {
        return targets;
    }

    public Entity getClosestTarget() {
        return closestTarget;
    }
}
