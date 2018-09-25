package game.data.spatial;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector2i;

public class QuadTree {
    int capacity = 4;
    Boundary2D boundary;
    ArrayList<Vector2i> points = new ArrayList<>();
    QuadTree[] children = new QuadTree[4];
    boolean hasDivided = false;

//    public static void main(String[] args) {
//        QuadTree qt = new QuadTree(new Boundary2D(0, 0, 64));
//        for (int i = 0; i < 1; i++) {
//            // qt.insert(new Vector2i(Misc.random(-32, 32), Misc.random(-32, 32)));
//            qt.insert(new Vector2i(2, -4));
//            qt.insert(new Vector2i(3, 3));
//            qt.insert(new Vector2i(7, 2));
//            qt.insert(new Vector2i(7, 4));
//            qt.insert(new Vector2i(3, 5));
//            qt.insert(new Vector2i(5, 1));
//            qt.insert(new Vector2i(15, 7));
//            qt.insert(new Vector2i(21, 7));
//            qt.insert(new Vector2i(25, 11));
//            qt.insert(new Vector2i(27, 15));
//            qt.insert(new Vector2i(30, 21));
//        }
//
//        System.out.println();
//        ArrayList<Vector2i> points = qt.findAllPoints(new Boundary2D(0, 0, 100));
//        // System.out.println("found " + points.size() + " points");
//        // for (Vector2i p: points) {
//        // System.out.println(p);
//        // }
//    }

    public QuadTree(Boundary2D boundary2D) {
        if (boundary2D == null) {
            System.err.println("Boundary cannot be null");
        }
        this.boundary = boundary2D;
    }

    public boolean insert(Vector2i point) {
        if (!boundary.contains(point)) {
            return false;
        }

        if (hasDivided) {
            // move all points to children
            for (int i = 0; i < children.length; i++) {
                for (Iterator<Vector2i> iter = points.iterator(); iter.hasNext();) {
                    Vector2i p = iter.next();
                    if (children[i].insert(p)) {
                        // System.out.println("my point moved to child");
                        iter.remove();
                    }
                }
            }
            for (int i = 0; i < children.length; i++) {
                if (children[i].insert(point)) {
                    // System.out.println("point sent to child");
                    return true;
                }
            }
        } else if (points.size() < capacity) {
            // add if it's a leaf node
            points.add(point);
            return true;
        } else {
            // divide
            int qSize = boundary.size / 4;
            children[0] = new QuadTree(new Boundary2D(boundary.x + qSize, boundary.y + qSize, qSize * 2));
            children[1] = new QuadTree(new Boundary2D(boundary.x + qSize, boundary.y - qSize, qSize * 2));
            children[2] = new QuadTree(new Boundary2D(boundary.x - qSize, boundary.y - qSize, qSize * 2));
            children[3] = new QuadTree(new Boundary2D(boundary.x - qSize, boundary.y + qSize, qSize * 2));
            hasDivided = true;
            // System.out.println("Divided " + boundary);

            // move all points to children
            for (int i = 0; i < children.length; i++) {
                for (Iterator<Vector2i> iter = points.iterator(); iter.hasNext();) {
                    Vector2i p = iter.next();
                    if (children[i].insert(p)) {
                        // System.out.println("my point moved to child");
                        iter.remove();
                    }
                }
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i].insert(point)) {
                    // System.out.println("point sent to child");
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Vector2i> findAllPoints(Boundary2D boundary2D) {
        ArrayList<Vector2i> result = new ArrayList<>();

        for (Vector2i point: points) {
            if (boundary2D.contains(point)) {
                // System.out.println("p " + point.x + " " + point.y + " inside ");
                // System.out.println(this.boundary);
                result.add(point);
            }
        }

        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                result.addAll(children[i].findAllPoints(boundary2D));
            }
        }

        return result;
    }

}
