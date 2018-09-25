package game.data.spatial;

import java.util.ArrayList;
import java.util.Iterator;

public class Octree {
    int capacity = 4;
    Boundary3D boundary;
    ArrayList<Point> points = new ArrayList<>();
    Octree[] children = new Octree[8];
    boolean hasDivided = false;

//    public static void main(String[] args) {
//        Octree qt = new Octree(new Boundary3D(0, 0, 0, 16));
//        for (int i = 0; i < 1; i++) {
//            // qt.insert(new Vector2i(Misc.random(-32, 32), Misc.random(-32, 32)));
//            qt.insert(new Point(2, -4, 3));
//        }
//
//        System.out.println();
//        ArrayList<Point> points = qt.findAllPoints(new Boundary3D(0, 0, 0, 100));
//        // System.out.println("found " + points.size() + " points");
//        // for (Vector2i p: points) {
//        // System.out.println(p);
//        // }
//    }

    public Octree(Boundary3D boundary) {
        if (boundary == null) {
            System.err.println("Boundary cannot be null");
        }
        this.boundary = boundary;
    }

    public boolean insert(Point point) {
        if (!boundary.contains(point)) {
            return false;
        }

        if (hasDivided) {
            // move all points to children
            for (int i = 0; i < children.length; i++) {
                for (Iterator<Point> iter = points.iterator(); iter.hasNext();) {
                    Point p = iter.next();
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
            float qSize = boundary.size / 4;

            // TOP
            // NW, NE, SE, SW
            children[0] = new Octree(
                    new Boundary3D(boundary.x - qSize, boundary.y + qSize, boundary.z - qSize, qSize * 2));
            children[1] = new Octree(
                    new Boundary3D(boundary.x + qSize, boundary.y + qSize, boundary.z - qSize, qSize * 2));
            children[2] = new Octree(
                    new Boundary3D(boundary.x + qSize, boundary.y + qSize, boundary.z + qSize, qSize * 2));
            children[3] = new Octree(
                    new Boundary3D(boundary.x - qSize, boundary.y + qSize, boundary.z + qSize, qSize * 2));

            // BOTTOM
            // NW, NE, SE, SW
            children[4] = new Octree(
                    new Boundary3D(boundary.x - qSize, boundary.y - qSize, boundary.z - qSize, qSize * 2));
            children[5] = new Octree(
                    new Boundary3D(boundary.x + qSize, boundary.y - qSize, boundary.z - qSize, qSize * 2));
            children[6] = new Octree(
                    new Boundary3D(boundary.x + qSize, boundary.y - qSize, boundary.z + qSize, qSize * 2));
            children[7] = new Octree(
                    new Boundary3D(boundary.x - qSize, boundary.y - qSize, boundary.z + qSize, qSize * 2));

            hasDivided = true;
            // System.out.println("Divided " + boundary);

            // move all points to children
            for (int i = 0; i < children.length; i++) {
                for (Iterator<Point> iter = points.iterator(); iter.hasNext();) {
                    Point p = iter.next();
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

    public ArrayList<Point> findAllPoints(Boundary3D boundary) {
        ArrayList<Point> result = new ArrayList<>();

        for (Point point: points) {
            if (boundary.contains(point)) {
                // System.out.println("p " + point.x + " " + point.y + " inside ");
                // System.out.println(this.boundary);
                result.add(point);
            }
        }

        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                result.addAll(children[i].findAllPoints(boundary));
            }
        }

        return result;
    }

    public void draw() {
        boundary.draw();
        for (Point point: points) {
            point.draw();
        }
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                children[i].draw();
            }
        }
    }

    public int getRemainingCapacity() {
        if (!hasDivided) {
            return capacity - points.size();
        }
        int total = 0;
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                total += children[i].getRemainingCapacity();
            }
        }
        
        return total;
    }

}
