package game.utils;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import engine.utils.Color;
import engine.utils.Vec3;
import game.Misc;
import game.data.IndexBuffer;
import game.data.Resources;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.graphics.Skybox;
import game.graphics.text.WorldText;

// TODO use this as a part of Collider component
public class AABB {
    private VertexArray vao;
    private IndexBuffer ibo;
    private Shader shader;
    private Vec3 min, max;
    // private Vec3 orgMin, orgMax;
    // private Matrix4f scaleMatrix;
    // private Vec3 lastPosition;
    private String name;
    private Transform localTransform, parent;

    // NOTE this is now a OBB because parent transform contains rotation
    public AABB(String name, Transform parent, Vec3 min, Vec3 max) {
        this.name = name;

        // NOTE scale won't update with the parent
        this.parent = new Transform(parent.getPosition(), new Vec3(), parent.getScale());
        this.min = new Vec3(min);
        this.max = new Vec3(max);
        shader = Resources.shaders.get(ShaderType.MISC);

        float border = 0.02f;

        float sx = max.x - min.x + border;
        float sy = max.y - min.y + border;
        float sz = max.z - min.z + border;

        float[] positions = new float[] { //

                // front
                -0.5f * sx, -0.5f * sy, 0.5f * sz, //
                0.5f * sx, -0.5f * sy, 0.5f * sz, //
                0.5f * sx, 0.5f * sy, 0.5f * sz, //
                -0.5f * sx, 0.5f * sy, 0.5f * sz, //
                // back
                -0.5f * sx, -0.5f * sy, -0.5f * sz, //
                0.5f * sx, -0.5f * sy, -0.5f * sz, //
                0.5f * sx, 0.5f * sy, -0.5f * sz, //
                -0.5f * sx, 0.5f * sy, -0.5f * sz, //

                // min.x, min.y, max.z, //
                // max.x, min.y, max.z, //
                // max.x, max.y, max.z, //
                // min.x, max.y, max.z, //
                //
                // min.x, min.y, min.z, //
                // max.x, min.y, min.z, //
                // max.x, max.y, min.z, //
                // min.x, max.y, min.z, //
        };

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();
        ibo = new IndexBuffer(Skybox.indices);
        ibo.bind();

        localTransform = new Transform(new Vec3((max.x + min.x) / 2, (max.y + min.y) / 2, (max.z + min.z) / 2),
                new Vec3(), 1);

        // updateScaleMatrix();

        // orgMin = new Vec3(min);
        // orgMax = new Vec3(max);

        // lastPosition = new Vec3();
    }

    // private void updateScaleMatrix() {
    // scaleMatrix = new Matrix4f();
    // scaleMatrix.setRow(0, new Vector4f(max.x - min.x, 1, 1, 1));
    // scaleMatrix.setRow(1, new Vector4f(1, max.y - min.y, 1, 1));
    // scaleMatrix.setRow(2, new Vector4f(1, 1, max.z - min.z, 1));
    // scaleMatrix.setRow(3, new Vector4f(1, 1, 1, 1));
    // }

    // public void updatePosition(Vec3 position) {
    // localTransform.setPosition(position);
    // if (!position.equals(lastPosition)) {
    // min = orgMin.add(position, new Vec3());
    // max = orgMax.add(position, new Vec3());
    // updateScaleMatrix();
    // lastPosition = new Vec3(position);
    // }
    // }

    public boolean canStepOn(AABB other) {
        return other.getMaxY() - getMinY() > 0 && other.getMaxY() - getMinY() < 0.5f;
    }

    public boolean collidesWith(AABB other) {
        if (this == other) {
            return false;
        }

        boolean width = getMaxX() > other.getMinX() && other.getMaxX() > getMinX();
        boolean height = getMaxY() > other.getMinY() && other.getMaxY() > getMinY();
        boolean depth = getMaxZ() > other.getMinZ() && other.getMaxZ() > getMinZ();

        // System.out.println(other.getName() + "'s maxX: " + getMaxX());

        return width && height && depth;
    }

    public boolean contains(Vec3 point) {
        // TODO fix it for 2D objects
        return Misc.isInRange(point.x, getMinX(), getMaxX()) && Misc.isInRange(point.y, getMinY(), getMaxY())
                && Misc.isInRange(point.z, getMinZ(), getMaxZ());
    }

    public void draw() {
        shader.bind();
        vao.bind();
        ibo.bind();
        shader.uploadMatrix4f("u_mMatrix", localTransform.getMatrix().mul(parent.getMatrix(), new Matrix4f()));
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        int polyMode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glDrawElements(GL11.GL_TRIANGLES, Skybox.indices.length, GL11.GL_UNSIGNED_INT, 0);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, polyMode);
        ibo.unbind();
        vao.unbind();
        shader.unbind();

        String minString = "MIN x:" + (parent.getPosition().x + min.x) + "y:" + (parent.getPosition().y + min.y) + "z:"
                + (parent.getPosition().z + min.z);
        String maxString = "MAX x:" + (parent.getPosition().x + max.x) + "y:" + (parent.getPosition().y + max.y) + "z:"
                + (parent.getPosition().z + max.z);
        WorldText.drawString(minString, new Vec3(parent.getPosition().add(min)), Color.RED, 1f);
        WorldText.drawString(maxString, new Vec3(parent.getPosition().add(max)), Color.RED, 1f);

    }

    public Vec3 getGlobalCenter() {
        return new Vec3((getMinX() + getMaxX()) / 2, (getMinY() + getMaxY()) / 2, (getMinZ() + getMaxZ()) / 2);

    }

    private Vec3 getGlobalMax() {
        // Vec3 vec3 = new Vec3(localTransform.getPosition());
        // parent.getPosition().sub(vec3, vec3);
        // vec3.add(max);
        // return vec3;

        Vector4f vec = new Vector4f(max.x, max.y, max.z, 1).mul(parent.getMatrix());
        return new Vec3(vec.x, vec.y, vec.z);
    }

    private Vec3 getGlobalMin() {
        // Vec3 vec3 = new Vec3(localTransform.getPosition());
        // parent.getPosition().sub(vec3, vec3);
        // vec3.add(min);
        // return vec3;

        Vector4f vec = new Vector4f(min.x, min.y, min.z, 1).mul(parent.getMatrix());
        // System.out.println(name + " " + vec);
        return new Vec3(vec.x, vec.y, vec.z);
    }

    public float getHeight() {
        return getMaxY() - getMinY();
    }

    public Vec3 getLocalCenter() {
        return new Vec3((min.x + max.x) / 2, (min.y + max.y) / 2, (min.z + max.z) / 2);
    }

    private float getMaxX() {
        return getGlobalMax().x;
    }

    public float getMaxY() {
        return getGlobalMax().y;
    }

    private float getMaxZ() {
        return getGlobalMax().z;
    }

    private float getMinX() {
        return getGlobalMin().x;
    }

    public float getMinY() {
        return getGlobalMin().y;
    }

    private float getMinZ() {
        return getGlobalMin().z;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectlyAbove(AABB other) {

        float xMin = other.getMinX();
        float xMax = other.getMaxX();
        float zMin = other.getMinZ();
        float zMax = other.getMaxZ();

        float yMax = other.getMaxY();

        if (Misc.isInRange((getMaxX() + getMinX()) / 2, xMin, xMax)) {
            if (Misc.isInRange((getMaxZ() + getMinZ()) / 2, zMin, zMax)) {
                if (Misc.isInRange(getMinY(), yMax, Float.POSITIVE_INFINITY)) {

                    // System.out.println(getMinY() + " " + other.getMaxY());

                    return true;
                }
            }
        }

        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

}
