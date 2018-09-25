package game.graphics.lighting;

import engine.utils.Vec3;

public class DirectionalLight {
    private Light light;
    private Vec3 direction;
    
    public DirectionalLight() {
        this.light = new Light(new Vec3(1, 1, 1), 1f);
        this.direction =  new Vec3(0, 1000, 1000).normalize();
    }

    public DirectionalLight(Light light, Vec3 direction) {
        this.light = light;
        this.direction = direction.normalized();
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction.normalize();
    }


}
