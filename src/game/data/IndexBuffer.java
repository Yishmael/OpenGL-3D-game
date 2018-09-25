package game.data;

import static org.lwjgl.opengl.GL15.glBindBuffer;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class IndexBuffer {

    private int buffer;
    private static int count;

    // TODO use indices sometime in the future
    public IndexBuffer(IntBuffer indices) {
        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        count++;
        if (count > 1000) {
            System.out.println("IBO #"+ count);
        }
    }
    
    public IndexBuffer(int[] indices) {
        this((IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip());
    }

    public void bind() {
        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer);
    }

    public void unbind() {
        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBuffer() {
        return buffer;
    }

    public void delete() {
        GL15.glDeleteBuffers(buffer);
        count--;
    }
}
