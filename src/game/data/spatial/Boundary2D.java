package game.data.spatial;

import org.joml.Vector2i;

public class Boundary2D {
    public int x, y, size;

    // coords are centered
    public Boundary2D(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public boolean contains(Vector2i point) {
        return point.x >= x - size / 2 && point.x < x + size / 2 && point.y >= y - size / 2 && point.y < y + size / 2;
    }

    @Override
    public String toString() {
        int lx = x - size / 2;
        int rx = x + size / 2;
        int uy = y + size / 2;
        int dy = y - size / 2;

        return "b " + x + " " + y + " " + size;

        // return "(" + lx + "," + uy + ") (" + rx + "," + uy + ")" + "\n" + //
        // "(" + lx + "," + dy + ") (" + rx + "," + dy + ")";

    }

}
