
package engine.utils;

import org.joml.Vector4f;

// TODO create Vec4
public class Color extends Vector4f {
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color PINK = new Color(1, 0, 1, 1);
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color CYAN = new Color(0, 1, 1, 1);

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }    
    public Color(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public Color(Color color) {
        this.x = color.x;
        this.y = color.y;
        this.z = color.z;
        this.w = color.w;
    }
}
