package game.meshes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import engine.GameObject;
import engine.components.Camera;
import engine.components.Component;
import engine.components.MeshRenderer;
import engine.utils.Vec3;
import game.Misc;
import game.data.VertexArray;
import game.data.VertexBuffer;
import game.graphics.Shader;
import game.utils.Transform;

public class Mesh implements Component {
    private FloatBuffer positions, texCoords, normals;
    private IntBuffer indices;
    private ArrayList<Vec3> minExtends = new ArrayList<>(), maxExtends = new ArrayList<>();
    private VertexArray vao;
    // private IndexBuffer ibo;
    private GameObject go;
    private Shader shader;
    private Texture texture;
    private boolean wireframe;

    // TODO enable mesh editing
    public Mesh() {
    }

    public Mesh(GameObject go) {
        this.go = go;
        if (!this.go.hasComponent(ComponentType.MESH_RENDERER)) {
            this.go.addComponent(MeshRenderer.class);
        }
    }

    public void bind() {
        shader.bind();
        vao.bind();
        // ibo.bind();
        if (texture != null) {
            texture.bind();
        }
    }

    public void unbind() {
        if (texture != null) {
            texture.unbind();
        }
        // ibo.unbind();
        vao.unbind();
        shader.unbind();
    }

    public void delete() {
        // ibo.delete();
        vao.delete();
    }

    public void prepare() {
        vao = new VertexArray();
        // ibo = new IndexBuffer(indices);
        vao.addBuffer(new VertexBuffer(positions, 0, 3));
        vao.addBuffer(new VertexBuffer(texCoords, 1, 2));
        vao.addBuffer(new VertexBuffer(normals, 2, 3));
        vao.prepare();
    }

    public void allocatePositions(int count) {
        positions = BufferUtils.createFloatBuffer(count);
    }

    public void allocateTexCoords(int count) {
        texCoords = BufferUtils.createFloatBuffer(count);
    }

    public void allocateNormals(int count) {
        normals = BufferUtils.createFloatBuffer(count);
    }

    public void allocateIndices(int count) {
        indices = BufferUtils.createIntBuffer(count);
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public FloatBuffer getPositions() {
        return positions;
    }

    public FloatBuffer getTexCoords() {
        return texCoords;
    }

    public IntBuffer getIndices() {
        return indices;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void putPositions(float[] p) {
        positions.put(p);
    }

    public void putTexCoords(float[] t) {
        texCoords.put(t);
    }

    public void putNormals(float[] n) {
        normals.put(n);
    }

    public int getModelCount() {
        return minExtends.size();
    }

    public Vec3 getMinExtends(int model) {
        return minExtends.get(model);
    }

    public Vec3 getMaxExtends(int model) {
        return maxExtends.get(model);
    }

    public void putIndex(int i) {
        indices.put(i);
    }

    public void flipBuffers() {
        positions.flip();
        if (texCoords != null) {
            texCoords.flip();
        }
        if (normals != null) {
            normals.flip();
        }
        if (indices != null) {
            indices.flip();
        }
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void addMessage(String message, Object... args) {
    }

    @Override
    public GameObject getAttachedGameObject() {
        return go;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MESH;
    }

    public void setUniforms() {
        // temp
        if (go.hasComponent(ComponentType.CAMERA)) {
            Camera camera = ((Camera) go.getComponent(ComponentType.CAMERA));
            shader.uploadMatrix4f("u_pMatrix", camera.getProjMatrix());
            Transform transform = new Transform(camera.getTransform());
            // transform.getPosition().x += -1;
            transform.getPosition().y += 1;
            transform.getPosition().z += 10;
            transform.setRotation(new Vec3());
            shader.uploadMatrix4f("u_vMatrix", Misc.createViewMatrix(transform));
        }
        shader.uploadMatrix4f("u_mMatrix", go.getTransform().getMatrix());
    }

    @Override
    public void update(float dt) {
    }

    public void set(Mesh mesh) {
        positions = mesh.positions;
        texCoords = mesh.texCoords;
        normals = mesh.normals;
        indices = mesh.indices;
    }

    @Override
    public void draw() {
    }

    public void setPositions(float[] positions) {
        this.positions = (FloatBuffer) BufferUtils.createFloatBuffer(positions.length).put(positions).flip();
    }

    public void setNormals(float[] normals) {
        this.normals = (FloatBuffer) BufferUtils.createFloatBuffer(normals.length).put(normals).flip();
    }

    public void setUvs(float[] uvs) {
        this.texCoords = (FloatBuffer) BufferUtils.createFloatBuffer(uvs.length).put(uvs).flip();
    }

    public void setIndices(int[] indices) {
        this.indices = (IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip();
    }

    public Shader getShader() {
        return shader;
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public void setModelCount(int modelCount) {
        while (minExtends.size() < modelCount) {
            minExtends.add(new Vec3(Float.POSITIVE_INFINITY));
        }
        while (maxExtends.size() < modelCount) {
            maxExtends.add(new Vec3(Float.NEGATIVE_INFINITY));
        }
    }
}
