package game.graphics.text;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import engine.utils.Color;
import engine.utils.Vec2;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.meshes.Texture;
import game.meshes.Texture.TextureType;
import game.time.Timer;
import game.utils.Loader;

public abstract class Text {

    protected static ArrayList<ScreenTextData> screenTexts = new ArrayList<>();
    protected static ArrayList<WorldTextData> worldTexts = new ArrayList<>();
    protected static Shader shader;
    protected static VertexArray vao;
    protected static Texture texture;
    protected static Vector2f[] locations;
    protected static float fadeTime = 0.15f;
    protected static Matrix4f identity = new Matrix4f();
    protected static HashMap<Character, Symbol> symbols = new HashMap<>();

    // TODO fix frame drop
    // TODO attach text above entities
    // TODO optionally make text always face the camera

    public static void init() {
        shader = Loader.loadShader(ShaderType.TEXT);
        texture = Loader.loadTexture(TextureType.TEXT);
        locations = new Vector2f[91];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Vector2f();
            locations[i].x = (i % 16) / 16f;
            locations[i].y = 1 - (i / 16) / 16f;
        }

        FloatBuffer positions = BufferUtils.createFloatBuffer(6 * 3);
        for (int i = 0; i < positions.capacity() / 6 / 3; i++) {
            positions.put(new float[] { -0.5f, -0.5f, 0 });
            positions.put(new float[] { 0.5f, -0.5f, 0 });
            positions.put(new float[] { 0.5f, 0.5f, 0 });

            positions.put(new float[] { 0.5f, 0.5f, 0 });
            positions.put(new float[] { -0.5f, 0.5f, 0 });
            positions.put(new float[] { -0.5f, -0.5f, 0 });
        }
        positions.flip();

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();
        vao.unbind();

        // load texture coordinates for every character
        for (int i = 0; i < locations.length; i++) {
            float left = locations[i].x;
            float right = left + 1 / 16f;
            float top = locations[i].y;
            float bottom = top - 1 / 16f;

            // sides are flipped
            float temp = bottom;
            bottom = top;
            top = temp;

            temp = left;
            left = right;
            right = temp;

            FloatBuffer texCoords = BufferUtils.createFloatBuffer(locations.length * 6 * 2);
            // upper left triangle
            texCoords.put(new float[] { right, top });
            texCoords.put(new float[] { left, top });
            texCoords.put(new float[] { left, bottom });

            // lower right triangle
            texCoords.put(new float[] { left, bottom });
            texCoords.put(new float[] { right, bottom });
            texCoords.put(new float[] { right, top });
            texCoords.flip();

            Symbol sym = new Symbol(positions, texCoords);
            symbols.put(Character.valueOf((char) (i + 32)), sym);

        }

    }

    public static void draw() {
        for (Iterator<ScreenTextData> iter = screenTexts.iterator(); iter.hasNext();) {
            ScreenTextData text = iter.next();
            if (Timer.now() - text.getCreationTime() < text.getDuration()) {
                ScreenText.drawString(text.getText(), text.getPosition(), text.getColor(), text.getSize());
            } else {
                Color color = text.getColor();
                ScreenText.drawString(text.getText(), text.getPosition(), color, text.getSize());
                color.w -= 1 / fadeTime / 60f;
                if (color.w <= 0) {
                    iter.remove();
                }
            }
        }
        for (Iterator<WorldTextData> iter = worldTexts.iterator(); iter.hasNext();) {
            WorldTextData text = iter.next();
            if (Timer.now() - text.getCreationTime() < text.getDuration()) {
                WorldText.drawString(text.getText(), text.getPosition(), text.getColor(), text.getSize());
            } else {
                // juice
                Color color = text.getColor();
                WorldText.drawString(text.getText(), text.getPosition(), color, text.getSize());
                color.w -= 1 / fadeTime / 60f;
                if (color.w <= 0) {
                    iter.remove();
                }
            }
        }
    }

}
