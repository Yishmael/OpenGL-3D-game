package game.data;

public class Tri {
    // private Vertex[] vertices;
    private int[] positions = new int[3];
    private int[] textureCoords = new int[3];
    private int[] normals = new int[3];

    public Tri() {
        for (int i = 0; i < 3; i++) {
            positions[i] = -1;
            textureCoords[i] = -1;
            normals[i] = -1;
        }
    }

    public void addVertexData(int posIndex, int texIndex, int normalIndex) {
        for (int i = 0; i < 3; i++) {
            if (positions[i] == -1) {
                positions[i] = posIndex;
                textureCoords[i] = texIndex;
                normals[i] = normalIndex;
                break;
            }
        }
    }

    public int getPosIndex(int i) {
        return positions[i];
    }
    
    public int getNormIndex(int i) {
        return normals[i];
    }
    
    public int getTexIndex(int i ) {
        return textureCoords[i];
    }
}
