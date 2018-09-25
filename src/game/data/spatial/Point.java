package game.data.spatial;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.data.Resources;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;

public class Point {
    public float x, y, z;
    private Shader shader;
    private VertexArray vao;

    // use geom shader to draw a cube
    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        shader = Resources.shaders.get(ShaderType.MISC);

        float[] positions = new float[] { //
                x, y, z//
        };

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();

        shader.bind();
        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
    }

    public void draw() {
        shader.bind();
        vao.bind();
        shader.uploadVector3f("u_color", new Vec3(0, 0, 0));
        shader.uploadMatrix4f("u_mMatrix", new Matrix4f());
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
        vao.unbind();
        shader.unbind();
    }
}
