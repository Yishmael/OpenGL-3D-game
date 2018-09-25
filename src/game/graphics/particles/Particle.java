package game.graphics.particles;

import java.nio.FloatBuffer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import engine.utils.Vec3;
import game.CoreCamera;
import game.Misc;
import game.data.Resources;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.entities.units.Player;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.meshes.Texture;
import game.meshes.Texture.TextureType;
import game.time.Timer;
import game.utils.Loader;
import game.utils.Transform;

public class Particle {
    private Shader shader;
    private Texture texture;
    private static VertexArray vao;
    private ParticleType type;
    private ParticleInfo info;
    private Transform initialTransform;
    private float startTime, fadeTime = 0.1f;
    private Fragment[] fragments;
    private int finishedFragmentsCount;

    protected Particle(ParticleType type, ParticleInfo info, Vec3 position) {
        this.type = type;
        this.info = info;
        this.initialTransform = new Transform(position, new Vec3(), type.getScale());
        shader = Resources.shaders.get(ShaderType.PARTICLE);

        texture = Loader.loadTexture(TextureType.valueOf(type.name()));

        fragments = new Fragment[info.getFragmentCount()];

        allocatePoint();

        startTime = Timer.now();
    }

    public ParticleType getType() {
        return type;
    }

    public void draw() {
        vao.bind();
        shader.bind();
        texture.bind();

        shader.uploadMatrix4f("u_pMatrix", CoreCamera.getProjMatrix());
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(Player.instance.getCamera()));
        for (Fragment frag: fragments) {
            if (frag == null) {
                break;
            }
            shader.uploadVector4f("u_color", frag.getColor());
            shader.uploadMatrix4f("u_mMatrix", frag.getTransform().getMatrix());
            GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
        }

        texture.unbind();
        shader.unbind();
        vao.unbind();
    }

    public void update() {
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] == null) {
                if (Timer.now() - startTime > info.getTimeBetweenFragments() * i) {
                    fragments[i] = new Fragment(initialTransform);
                    ParticleSystem.fragmentCount++;
                }
            }
            if (fragments[i] == null) {
                break;
            }

            float x = info.getFragmentVelocities()[i].x;
            float y = info.getFragmentVelocities()[i].y;
            float z = info.getFragmentVelocities()[i].z;

//            float angleXY = Math.abs(Misc.atan(y / x));
//            float angleXZ = Math.abs(Misc.atan(z / x));

            float facX = 1;//Misc.cos(angleXY) * Misc.cos(angleXZ);
            float facY = 1;//Misc.sin(angleXY);
            float facZ = 1;//Misc.sin(angleXZ);

            fragments[i].getTransform().translate(new Vec3(x * facX, y * facY, z * facZ));
            // fragments[i].getTransform().rotate(new Vec3(Misc.random(-Misc.PI, Misc.PI)));

            if (Timer.now() - fragments[i].getCreationTime() > info.getFragmentDuration()) {
                fragments[i].deltaOpacity(-1 / fadeTime / 60f);

                if (fragments[i].getOpacity() <= 0) {
                    if (info.isLooping()) {
                        fragments[i].reset();
                    } else {
                        if (!fragments[i].hasFinished()) {
                            fragments[i].finish();
                            finishedFragmentsCount++;
                            ParticleSystem.fragmentCount--;
                        }
                    }
                }
            }
        }
    }

    protected int getFragmentCount() {
        return info.getFragmentCount();
    }

    public boolean hasEnded() {
        return finishedFragmentsCount == fragments.length;
    }

    private static void allocatePoint() {
        FloatBuffer positions = BufferUtils.createFloatBuffer(3);

        // TODO make particles instanced
        positions.put(new float[] { //
                0, 0, 0, //
        }).flip();

        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.prepare();
    }
}
