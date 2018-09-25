package engine.components;

import org.lwjgl.opengl.GL11;

import engine.GameObject;
import game.meshes.Mesh;

public class MeshRenderer implements Component {

    protected GameObject go;

    public MeshRenderer(GameObject go) {
        this.go = go;
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
        return ComponentType.MESH_RENDERER;
    }

    @Override
    public void update(float dt) {
        // Mesh mesh = (Mesh) go.getComponent(ComponentType.mesh);
        // draw vertices from mesh
    }

    @Override
    public String toString() {
        return "MeshRenderer: ";
    }

    @Override
    public void draw() {
        Mesh mesh = (Mesh) go.getComponent(ComponentType.MESH);
        if (mesh != null) {
            mesh.bind();
            mesh.setUniforms();
            if (mesh.isWireframe()) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            } else {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            }
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getIndices().capacity());
            mesh.unbind();
        }
    }

}
