package game.data;

import java.util.ArrayList;

import org.lwjgl.opengl.GL30;

public class VertexArray {

    private int array;
    private ArrayList<VertexBuffer> vbos = new ArrayList<>();

    private static int count;

    public VertexArray() {
        // TODO request new vertex array only once per primitive
        array = GL30.glGenVertexArrays();
        count++;
        
        if (count > 1000) {
            System.out.println("VAO #" +count);
        }
    }

    public void bind() {
        GL30.glBindVertexArray(array);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void addBuffer(VertexBuffer vbo) {
        vbos.add(vbo);
    }

    public void delete() {
        bind();
        for (VertexBuffer vbo: vbos) {
            vbo.delete();
        }
        vbos.clear();
        unbind();
        GL30.glDeleteVertexArrays(array);
        count--;
    }

    public void prepare() {
        bind();
        for (VertexBuffer vbo: vbos) {
            vbo.prepare();
        }
        unbind();
    }

    public void replaceBuffer(int i, VertexBuffer vbo) {
        vbos.remove(i);
        vbos.add(vbo);
        vbo.prepare();
    }

    public int getBufferCount() {
        return vbos.size();
    }

    public void deleteBuffers() {
        new Exception().printStackTrace();
        System.out.println("vao deleted");
        bind();
        for (VertexBuffer vbo: vbos) {
            vbo.delete();
        }
        unbind();
    }
}
