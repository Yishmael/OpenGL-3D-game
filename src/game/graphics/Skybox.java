package game.graphics;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import engine.CoreEngine;
import engine.GameObject;
import engine.components.Component.ComponentType;
import game.CoreCamera;
import game.Misc;
import game.data.IndexBuffer;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.units.Player;
import game.graphics.Shader.ShaderType;
import game.utils.Loader;

public abstract class Skybox {
    public enum SkyboxType {
        ICEFLATS, SIEGE;
    }

    private static VertexArray vao;
    private static int texture;
    private static Shader shader;
    private static IndexBuffer ibo;
    public static float[] positions = new float[] { //
            // front
            -1.0f, -1.0f, 1.0f, //
            1.0f, -1.0f, 1.0f, //
            1.0f, 1.0f, 1.0f, //
            -1.0f, 1.0f, 1.0f, //
            // back
            -1.0f, -1.0f, -1.0f, //
            1.0f, -1.0f, -1.0f, //
            1.0f, 1.0f, -1.0f, //
            -1.0f, 1.0f, -1.0f, //
    };

    public static int[] indices = new int[] { //
            // front
            0, 1, 2, //
            2, 3, 0, //
            // right
            1, 5, 6, //
            6, 2, 1, //
            // back
            7, 6, 5, //
            5, 4, 7, //
            // left
            4, 0, 3, //
            3, 7, 4, //
            // bottom
            4, 5, 1, //
            1, 0, 4, //
            // top
            3, 2, 6, //
            6, 7, 3, //
    };

    public static void init() {
        SkyboxType type = SkyboxType.ICEFLATS;

        shader = Loader.loadShader(ShaderType.SKYBOX);

        texture = Loader.loadSkyboxTexture(type.name());

        FloatBuffer fb = BufferUtils.createFloatBuffer(positions.length);
        fb.put(positions).flip();

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(fb, 0, 3));
        vao.prepare();

        ibo = new IndexBuffer(indices);

        shader.bind();
        shader.uploadInteger("u_texture", 0);
        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
        shader.unbind();
    }

    public static void draw() {
        // temp
        if (Player.instance == null) {

            for (GameObject go: CoreEngine.gameObjects) {
                if (go.hasComponent(ComponentType.CAMERA)) {
                    shader.bind();
                    shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
                    break;
                }
            }
        }

        GL11.glDepthMask(false);
        GL11.glCullFace(GL11.GL_FRONT);
        shader.bind();
        vao.bind();
        ibo.bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);

        // ?TODO add global view Matrix that camera updates when moving
        Matrix4f viewMatrix = Misc.createSkyboxViewMatrix(CoreCamera.instance.getRotation());
        shader.uploadMatrix4f("u_vMatrix", viewMatrix);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
        vao.unbind();
        shader.unbind();
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(true);
    }

}
