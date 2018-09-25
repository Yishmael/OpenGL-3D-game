package game.data.spatial;

import org.lwjgl.opengl.GL11;

import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.data.IndexBuffer;
import game.data.Resources;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.graphics.Skybox;

public class Boundary3D {
    public float x, y, z, size;
    private VertexArray vao;
    private IndexBuffer ibo;
    private Shader shader;

    // coords are centered
    public Boundary3D(float x, float y, float z, float size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;

        shader = Resources.shaders.get(ShaderType.MISC);

        float[] positions = new float[] { //
                x - size / 2, y - size / 2, z + size / 2, //
                x + size / 2, y - size / 2, z + size / 2, //
                x + size / 2, y + size / 2, z + size / 2, //
                x - size / 2, y + size / 2, z + size / 2, //

                x - size / 2, y - size / 2, z - size / 2, //
                x + size / 2, y - size / 2, z - size / 2, //
                x + size / 2, y + size / 2, z - size / 2, //
                x - size / 2, y + size / 2, z - size / 2, //
        };

        // ?TODO load all boundaries in the same vao (boundaries never change)
        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();
        ibo = new IndexBuffer(Skybox.indices);
        ibo.bind();

        shader.bind();
        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
    }

    // TODO don't draw diagonal lines
    public void draw() {
        shader.bind();
        vao.bind();
        ibo.bind();
        shader.uploadVector3f("u_color", new Vec3(0, 0, 0));
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        int polyMode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glDrawElements(GL11.GL_TRIANGLES, Skybox.indices.length, GL11.GL_UNSIGNED_INT, 0);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, polyMode);
        ibo.unbind();
        vao.unbind();
        shader.unbind();
    }

    public boolean contains(Point point) {
        return point.x >= x - size / 2 && point.x < x + size / 2 && //
                point.y >= y - size / 2 && point.y < y + size / 2 && //
                point.z >= z - size / 2 && point.z < z + size / 2;
    }

    @Override
    public String toString() {
        float lx = x - size / 2;
        float rx = x + size / 2;
        float uy = y + size / 2;
        float dy = y - size / 2;
        float uz = z + size / 2;
        float dz = z - size / 2;

        return "b " + x + " " + y + " " + z + " " + size;

        // return "(" + lx + "," + uy + ") (" + rx + "," + uy + ")" + "\n" + //
        // "(" + lx + "," + dy + ") (" + rx + "," + dy + ")";

    }

}
