package game.graphics.materials;

import engine.utils.Color;
import engine.utils.Vec3;
import game.graphics.Shader;
import game.graphics.lighting.DirectionalLight;

public class BasicMaterial extends Material {
    private DirectionalLight directionalLight;
    // temp

    public BasicMaterial(Shader shader, DirectionalLight directionalLight) {
        super(shader);
        this.directionalLight = directionalLight;
    }

    // TODO do this in the shader
    @Override
    public void applyMaterial() {
        shader.bind();

        shader.uploadVector3f("directionalLight.light.color", directionalLight.getLight().getColor());
        shader.uploadFloat("directionalLight.light.intensity", directionalLight.getLight().getIntensity());
        shader.uploadVector3f("directionalLight.direction", directionalLight.getDirection());

        shader.uploadVector3f("otherLight.light.color", new Vec3(1f, 0, 0));
        shader.uploadFloat("otherLight.light.intensity", 1f);
        shader.uploadVector3f("otherLight.direction", new Vec3(0, -1, 0));

        shader.uploadFloat("u_alpha", 1 - transparency);
        shader.unbind();
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

}
