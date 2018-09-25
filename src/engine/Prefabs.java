package engine;

import engine.GameObject.PrimitiveType;
import engine.components.Component.ComponentType;
import engine.components.RigidBody;
import engine.components.colliders.Plane;
import engine.components.colliders.Sphere;
import engine.scripts.CollisionResponse;
import engine.scripts.ScriptEngine.Command;
import engine.utils.Vec3;
import game.Misc;
import game.data.Resources;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.graphics.lighting.DirectionalLight;
import game.graphics.materials.BasicMaterial;
import game.graphics.materials.Material;
import game.meshes.Mesh;
import game.meshes.Texture.TextureType;

public class Prefabs {
    private static Shader shader;

    public static void init() {
        shader = Resources.shaders.get(ShaderType.BASIC);
        shader.bind();
        // shader.setMatrix4f("u_pMatrix", new Camera(null).getProjMatrix());
        shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(new Vec3(0, 0, 10), new Vec3()));
        shader.uploadMatrix4f("u_pMatrix", Misc.createPerspectiveMatrix(90, 0.001f, 1000f));
        DirectionalLight dirLight = new DirectionalLight();
        dirLight.setDirection(new Vec3(100, 1000, 1000));
        Material material = new BasicMaterial(shader, dirLight);
        material.applyMaterial();
    }

    public static GameObject createCube() {
        GameObject go = new GameObject("Cube1");
        go.addComponent(Mesh.class);
        Mesh mesh = (Mesh) go.getComponent(ComponentType.MESH);
        // mesh.setMaterial(new Material(shader, directionalLight));
        mesh.setShader(shader);
        mesh.set(Resources.meshes.get(PrimitiveType.CUBE));
        mesh.setTexture(Resources.textures.get(TextureType.GRASS));
        mesh.prepare();

        return go;
    }

    public static GameObject createProjectile() {
        GameObject go = new GameObject("Proj1");
        go.getTransform().setScale(0.2f);
        go.setLifetime(1);

        go.addComponent(Mesh.class);
        Mesh mesh = (Mesh) go.getComponent(ComponentType.MESH);
        // mesh.setMaterial(new Material(shader, directionalLight));
        mesh.setShader(shader);
        mesh.set(Resources.meshes.get(PrimitiveType.ICOSPHERE));
        mesh.setTexture(Resources.textures.get(TextureType.METAL));
        mesh.prepare();

        // go.addComponent(RigidBody.class);
        // RigidBody rb = (RigidBody) go.getComponent(ComponentType.RIGID_BODY);
        // rb.setMass(0.1f);
        // rb.setVelocity(new Vec3(0, 0, 0));

        go.addComponent(Sphere.class);
        Sphere sphere = (Sphere) go.getComponent(ComponentType.COLLIDER);
        sphere.setRelativePosition(new Vec3());
        sphere.setRadius(0.5f);

        go.cr = new CollisionResponse("Sphere1", Command.KILL);

        return go;
    }

    public static GameObject createSphere() {
        GameObject go = new GameObject("Sphere1");
        go.getTransform().setPosition(new Vec3(0, 1.5f, 0));
        go.getTransform().setPosition(new Vec3(6, 5.5f, 0));

        go.addComponent(Mesh.class);
        Mesh mesh = (Mesh) go.getComponent(ComponentType.MESH);
        mesh.setShader(shader);
        mesh.set(Resources.meshes.get(PrimitiveType.ICOSPHERE));
        mesh.setTexture(Resources.textures.get(TextureType.GRASS));
        mesh.prepare();

        go.addComponent(RigidBody.class);
        RigidBody rb = (RigidBody) go.getComponent(ComponentType.RIGID_BODY);
        rb.setMass(10);

        go.addComponent(Sphere.class);
        Sphere sphere = (Sphere) go.getComponent(ComponentType.COLLIDER);
        sphere.setRelativePosition(new Vec3());
        sphere.setRadius(1f);

        go.cr = new CollisionResponse("Proj1", Command.KILL);

        return go;
    }

    public static GameObject createPlane() {
        GameObject go = new GameObject("Plane1");
        go.getTransform().setPosition(new Vec3(0, -3, 0));
        go.getTransform().setScale(100);

        go.addComponent(Mesh.class);
        Mesh mesh = (Mesh) go.getComponent(ComponentType.MESH);
        mesh.setShader(shader);
        mesh.set(Resources.meshes.get(PrimitiveType.PLANE));
        mesh.setTexture(Resources.textures.get(TextureType.WOOD));
        // mesh.setWireframe(true);
        mesh.prepare();

        go.addComponent(RigidBody.class);
        RigidBody rb = (RigidBody) go.getComponent(ComponentType.RIGID_BODY);
        rb.setImmovable(true);

        go.addComponent(Plane.class);
        Plane plane = (Plane) go.getComponent(ComponentType.COLLIDER);
        plane.setNormal(new Vec3(0, 1, 0));

        return go;
    }

}
