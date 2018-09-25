package game.data;

import engine.utils.Vec2;
import engine.utils.Vec3;

public class Vertex {
    private Vec3 position;
    private Vec3 normal;
    private Vec2 uv;

    public Vertex(Vec3 position, Vec3 normal, Vec2 uv) {
        this.position = position;
        this.normal = normal;
        this.uv = uv;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec2 getUv() {
        return uv;
    }

    public void setNormal(Vec3 normal) {
        this.normal = normal;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setUv(Vec2 uv) {
        this.uv = uv;
    }
}
