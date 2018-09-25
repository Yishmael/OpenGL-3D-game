package game.graphics.text;

import java.nio.FloatBuffer;

import game.data.VertexArray;
import game.data.VertexBuffer;

public class Symbol {
    private FloatBuffer positions, texCoords;
    private VertexArray vao;

    public Symbol(FloatBuffer positions, FloatBuffer texCoords) {
        this.positions = positions;
        this.texCoords = texCoords;
        vao = new VertexArray();
        vao.addBuffer(new VertexBuffer(this.positions, 0, 3));
        vao.addBuffer(new VertexBuffer(this.texCoords, 1, 2));
        vao.prepare();
        vao.unbind();
    }

    public VertexArray getVao() {
        return vao;
    }
}
