package game.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import engine.utils.Color;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.Misc;
import game.data.IndexBuffer;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.Entity;
import game.entities.interatives.Interactive;
import game.graphics.Shader.ShaderType;
import game.graphics.text.ScreenText;
import game.meshes.Texture;
import game.meshes.Texture.TextureType;
import game.utils.Loader;

public abstract class HUD {
    private static VertexArray vao;
    private static VertexArray vaoCross;
    private static Shader shader;
    private static IndexBuffer ibo;
    private static int[] indices;
    private static Texture hudTexture;
    private static Texture crosshairTexture;

    public static void init() {
        shader = Loader.loadShader(ShaderType.HUD);
        shader.bind();
        Matrix4f matrix = Misc.createTransformationMatrix(new Vec3(), new Vec3(), 0.5f);
        matrix.translate(new Vector3f(0, 0, 0));
        matrix.scale(new Vector3f(4f, 4f, 1));
        shader.uploadMatrix4f("u_mMatrix", matrix);
        shader.uploadMatrix4f("u_pMatrix", Misc.createOrthographicMatrix());

        int numSquares = 1;
        FloatBuffer positions = BufferUtils.createFloatBuffer(numSquares * 4 * 2);
        for (int i = 0; i < positions.capacity() / 4 / 2; i++) {
            int perRow = 80;
            int x = i % perRow;
            int y = i / perRow;
            x = y = 0;
            positions.put(new float[] { -0.5f + x, -0.5f - y });
            positions.put(new float[] { 0.5f + x, -0.5f - y });
            positions.put(new float[] { 0.5f + x, 0.5f - y });
            positions.put(new float[] { -0.5f + x, 0.5f - y });
        }
        positions.flip();
        indices = new int[numSquares * 6];
        for (int i = 0; i < indices.length / 6; i++) {
            indices[i * 6 + 0] = i * 4 + 0;
            indices[i * 6 + 1] = i * 4 + 1;
            indices[i * 6 + 2] = i * 4 + 2;
            indices[i * 6 + 3] = i * 4 + 2;
            indices[i * 6 + 4] = i * 4 + 3;
            indices[i * 6 + 5] = i * 4 + 0;
        }
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(numSquares * 6 * 2);
        for (int i = 0; i < texCoords.capacity() / 6 / 2; i++) {
            texCoords.put(new float[] { 0, 0 });
            texCoords.put(new float[] { 1, 0 });
            texCoords.put(new float[] { 1, 1 });

            texCoords.put(new float[] { 0, 1 });
            texCoords.put(new float[] { 0, 0 });
            texCoords.put(new float[] { 1, 0 });
        }
        texCoords.flip();

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 2));
        vao.addBuffer(new VertexBuffer(texCoords, 1, 2));
        vao.addBuffer(new VertexBuffer(new float[] { //
                0, 1, 0, 0, //
                0, 1, 0, 0, //
                0, 1, 0, 0, //
                0, 1, 0, 0//
        }, 2, 4));//
        vao.prepare();

        IntBuffer inds = BufferUtils.createIntBuffer(indices.length);
        inds.put(indices).flip();
        ibo = new IndexBuffer(inds);

        vao.unbind();
        shader.unbind();

        vaoCross = new VertexArray();
        float size = 0.04f / 0.5f;
        vaoCross.addBuffer(new VertexBuffer(new float[] { //
                -0.5f * size, -0.5f * size, //
                0.5f * size, -0.5f * size, //

                0.5f * size, 0.5f * size, //
                -0.5f * size, 0.5f * size,//
        }, 0, 2));
        vaoCross.addBuffer(new VertexBuffer(texCoords, 1, 2));
        vaoCross.addBuffer(new VertexBuffer(new float[] { //
                1f, 0f, 0f, 0f, //
                1f, 0f, 0f, 0f, //

                1f, 0f, 0f, 0f, //
                1f, 0f, 0f, 0f, //
        }, 2, 4));
        vaoCross.prepare();

        hudTexture = Loader.loadTexture(TextureType.HUD);
        crosshairTexture = Loader.loadTexture(TextureType.CROSSHAIR);
    }

    private static Interactive interactiveObject;

    public static void setInteractiveObject(Interactive object) {
        interactiveObject = object;

    }

    public static void draw() {
        GL11.glDepthMask(false);
        shader.bind();
        vao.bind();
        ibo.bind();
        hudTexture.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
        hudTexture.unbind();
        ibo.unbind();
        vao.unbind();

        vaoCross.bind();
        ibo.bind();
        crosshairTexture.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
        crosshairTexture.unbind();
        ibo.unbind();
        vaoCross.unbind();
        shader.unbind();
        GL11.glDepthMask(true);

        if (interactiveObject != null) {
            ScreenText.drawString(interactiveObject.getMessage(),
                    new Vec2(-interactiveObject.getMessage().length() / 2 / 25f, -0.4f), Color.WHITE, 8f);
        }
    }

    public static Entity getInteractiveObject() {
        return (Entity) interactiveObject;
    }

    public static void clearInteractiveObject() {
        interactiveObject = null;
    }

}
