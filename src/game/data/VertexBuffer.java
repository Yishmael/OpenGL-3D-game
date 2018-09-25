package game.data;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class VertexBuffer {

    private int buffer;
    private FloatBuffer data;
    private int attribIndex, dimensions;
    private static int count;

    public VertexBuffer(float[] data, int attribIndex, int dimensions) {
        this((FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip(), attribIndex, dimensions);
    }

    // TODO load all data in one buffer (pos, norm, tex, col)
    public VertexBuffer(FloatBuffer data, int attribIndex, int dimensions) {
        if (data == null) {
            return;
        }
        this.data = data;
        this.attribIndex = attribIndex;
        this.dimensions = dimensions;
        buffer = GL15.glGenBuffers();
        
        count++;   
        if (count > 1000) {
            System.out.println("VBO #" + count);
        }
    }
    
    protected void prepare() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(attribIndex);
        GL20.glVertexAttribPointer(attribIndex, dimensions, GL11.GL_FLOAT, false, dimensions * 4, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    protected void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
    }

    protected void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    protected int getBuffer() {
        return buffer;
    }

    public void delete() {
        GL15.glDeleteBuffers(buffer);
        count--;
    }

}
