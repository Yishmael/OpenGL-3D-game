package game.meshes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import game.Misc;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.meshes.Texture.TextureType;
import game.utils.Loader;

public class Terrain {

    private Texture texture;
    private int width, height;
    private float[][] heightMap;
    private Mesh mesh;

    // TODO fix terrain..
    public Terrain() {

        loadHeightMap("heightmap");
        float[] positions = new float[height * width * 3 * 3];
        float[] normals = new float[height * width * 3 * 3];
        float[] textureCoords = new float[height * width * 2 * 3];
        int i = 0;
        for (int x = 0; x < width * height * 3f; x++) {
            normals[i * 3 + 0] = 0;
            normals[i * 3 + 1] = 1;
            normals[i * 3 + 2] = 0;

            textureCoords[i * 2 + 0] = x;
            textureCoords[i * 2 + 1] = x / 10 % 10;

            i++;
        }
        // back left 0, 0, 0
        // back right 1, 0, 0
        // front left 0, 0, 1
        int j = 0;
        int rows = 2;
        for (int k = 1; k <= 4; k++) {
            i = 0;
            for (; j < positions.length * k / 4; j += 9) {
                positions[j + 0] = k % rows + (j >= positions.length / 2 ? 1 : 0);
                positions[j + 1] = heightMap[j % 10][j / 10 % 10];
                positions[j + 2] = 0 + i;

                positions[j + 3] = k % rows + 1 - (j >= positions.length / 2 ? 1 : 0);
                positions[j + 4] = heightMap[j % 10][j / 10 % 10];
                positions[j + 5] = (j >= positions.length / 2 ? 1 : 0) + i;

                positions[j + 6] = k % rows + (j >= positions.length / 2 ? 1 : 0);
                positions[j + 7] = heightMap[j % 10][j / 10 % 10];
                positions[j + 8] = 1 + i;
                i++;
            }
        }

        texture = Loader.loadTexture(TextureType.ROCK);

        Shader shader = Loader.loadShader(ShaderType.TERRAIN);
        shader.bind();
        shader.uploadMatrix4f("u_pMatrix", Player.instance.getCamera().getProjMatrix());
        shader.unbind();

        
        // TODO use primitive restart to render terrain using indices
        
        mesh = new Mesh();
        mesh.setShader(shader);
        mesh.setPositions(positions);
        mesh.setNormals(normals);
        mesh.setUvs(textureCoords);
        mesh.prepare();
    }

    public void draw() {
        mesh.bind();
        texture.bind();
//        GL11.glEnable(GL31.GL_PRIMITIVE_RESTART);
//        GL31.glPrimitiveRestartIndex(index);
        mesh.getShader().uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        int mode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getPositions().capacity() / 3);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, mode);
//        GL11.glDisable(GL31.GL_PRIMITIVE_RESTART);
        texture.unbind();
        mesh.unbind();
    }

    private void loadHeightMap(String name) {
        String fullpath = "res/maps/" + name + ".png";
        try {
            BufferedImage image = ImageIO.read(new File(fullpath));
            width = image.getWidth();
            height = image.getHeight();
            heightMap = new float[height][width];
            float min = 0f;
            float max = 1f;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    float h = (image.getRGB(i, j) & 0xff) / 255f * (max - min) + min;
                    heightMap[i][j] = h;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
