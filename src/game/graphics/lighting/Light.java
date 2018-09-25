package game.graphics.lighting;

import engine.utils.Vec3;

public class Light {
    private Vec3 color;
    private float intensity;

    public Light(Vec3 color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Vec3 getColor() {
        return color;
    }
    
    public float getIntensity() {
        return intensity;
    }
}
