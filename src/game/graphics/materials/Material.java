package game.graphics.materials;

import game.graphics.Shader;

public abstract class Material {
    protected Shader shader;

    protected float transparency;

    public void setTransparency(float transparency) {
        this.transparency = Math.min(Math.max(transparency, 0), 1);
    }


    public Material(Shader shader) {
        this.shader = shader;
    }

    public abstract void applyMaterial(); 
    
    public Shader getShader() {
        return shader;
    } 
}
