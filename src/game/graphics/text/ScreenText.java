package game.graphics.text;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import engine.utils.Color;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.Misc;

public abstract class ScreenText extends Text {

    public static void addTimedText(String string, Vec2 position, Color color, float size, float duration) {
        screenTexts.add(new ScreenTextData(string, position, color, size, duration));
    }

    // TODO allow option to center the text on the screen
    public static void drawString(String string, Vec2 position, Color color, float size) {
        if (string.length() == 0) {
            return;
        }
        size /= 100f;

        shader.bind();
        vao.bind();
        texture.bind();

        shader.uploadVector4f("u_color",  color);
        shader.uploadMatrix4f("u_vMatrix", identity);
        shader.uploadMatrix4f("u_pMatrix", Misc.createOrthographicMatrix());

        Vec3 offset = new Vec3(position.x, position.y, 0);
        for (int i = 0; i < string.length(); i++) {
            char c = string.toCharArray()[i];

            offset.x += i > 0 ? size / 1.8f : 0;
            if (c == '\n') {
                offset.y -= size / 1.1f;
                offset.x = position.x - size / 1.8f;
                continue;
            }

            symbols.get(c).getVao().bind();

            Matrix4f modelMatrix = Misc.createTransformationMatrix(offset, new Vec3(), size);
            shader.uploadMatrix4f("u_mMatrix", modelMatrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
            symbols.get(c).getVao().unbind();
        }
        texture.unbind();
        vao.unbind();
        shader.unbind();

    }

}
