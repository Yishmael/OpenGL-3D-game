package game.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import engine.GameObject.PrimitiveType;
import engine.utils.Vec2;
import engine.utils.Vec3;
import game.Misc;
import game.data.Resources;
import game.data.Tri;
import game.entities.Entity;
import game.graphics.Shader;
import game.graphics.Shader.ShaderType;
import game.meshes.Mesh;
import game.meshes.Texture;
import game.meshes.Texture.TextureType;

public class Loader {
    // loading 1,000,000 cubes takes 150s
    // TODO add collision boxes in blender for AABB coords
    // ?TODO calculate mesh bounding boxes from face information
    public static Mesh loadMesh(PrimitiveType type) {
        return loadMesh(type, false);
    }
    
    @SuppressWarnings("null")
    private static Mesh loadMesh(PrimitiveType type, boolean multiObject) {
        if (Resources.meshes.containsKey(type)) {
            return Resources.meshes.get(type);
        }
        String name = type.toString().toLowerCase();
        String fullpath = "/meshes/" + name + ".obj";
        Mesh mesh = new Mesh();
        Vec3[] positions = null;
        Vec2[] texCoords = null;
        Vec3[] normals = null;

        int modelCount = 0;
        int lineCount = 0;
        int positionCount = 0;
        int texCoordCount = 0;
        int normalCount = 0;
        int faceCount = 0;
        boolean triangulated = false;

        String[] lines = new String[100000];
        Scanner scanner = null;
        if (Misc.EXPORTING) {
            scanner = new Scanner(Loader.class.getResourceAsStream(fullpath));
        } else {
            try {
                scanner = new Scanner(new File("res" + fullpath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Scanner scanner = new Scanner("res/meshes/" + name + ".obj");
        while (scanner.hasNextLine()) {
            lines[lineCount] = scanner.nextLine();
            String token = lines[lineCount].split(" ")[0];

            if (token.equals("o")) {
                modelCount++;
            } else if (token.equals("v")) {
                positionCount++;
            } else if (token.equals("vt")) {
                texCoordCount++;
            } else if (token.equals("vn")) {
                normalCount++;
            } else if (token.equals("f")) {
                // accounting for non-triangulated faces
                faceCount += lines[lineCount].split(" ").length - 3;
            }

            lineCount++;
        }
        scanner.close();

        mesh.setModelCount(modelCount);
        mesh.allocateIndices(faceCount * 3);

        positions = new Vec3[faceCount * 3];
        texCoords = new Vec2[texCoordCount];
        normals = new Vec3[faceCount * 3];

        // positions are indexed so they will stay unique in memory
        mesh.allocatePositions(positions.length * 3);

        if (texCoordCount > 0) {
            mesh.allocateTexCoords(faceCount * 3 * 2);
        }
        mesh.allocateNormals(faceCount * 3 * 3);

        int currentModel = -1;
        int i = 0, j = 0, k = 0;
        for (int l = 0; l < lineCount; l++) {
            String line = lines[l];
            String[] tokens = line.split(" ");
            if (tokens[0].equals("o")) {
                currentModel++;
                if (multiObject && currentModel > 0) {
                    
                }
//                 System.out.println(tokens[1]);
            } else if (tokens[0].equals("v")) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                positions[i] = new Vec3(x, y, z);
                i++;
            } else if (tokens[0].equals("vt")) {
                float u = Float.parseFloat(tokens[1]);
                float v = Float.parseFloat(tokens[2]);
                texCoords[j] = new Vec2(u, v);
                j++;
            } else if (tokens[0].equals("vn")) {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                normals[k] = new Vec3(x, y, z);
                k++;
            } else if (tokens[0].equals("f")) {
                Tri[] tris = parseFace(line.substring(2).split(" "));
                // vertex indices: face[0], face[3], face[6]
                // texture coord indices: face[1], face[4], face[7]
                // normal indices: face[2], face[5], face[8]

                for (Tri tri: tris) {

                    mesh.putIndex(tri.getPosIndex(0));
                    mesh.putIndex(tri.getPosIndex(1));
                    mesh.putIndex(tri.getPosIndex(2));

                    for (int p = 0; p < 3; p++) {
                        float x = positions[tri.getPosIndex(p)].x;
                        if (x <= mesh.getMinExtends(currentModel).x) {
                            mesh.getMinExtends(currentModel).x = x;
                        }
                        if (x >= mesh.getMaxExtends(currentModel).x) {
                            mesh.getMaxExtends(currentModel).x = x;
                        }
                        float y = positions[tri.getPosIndex(p)].y;
                        if (y <= mesh.getMinExtends(currentModel).y) {
                            mesh.getMinExtends(currentModel).y = y;
                        }
                        if (y >= mesh.getMaxExtends(currentModel).y) {
                            mesh.getMaxExtends(currentModel).y = y;
                        }
                        float z = positions[tri.getPosIndex(p)].z;
                        if (z <= mesh.getMinExtends(currentModel).z) {
                            mesh.getMinExtends(currentModel).z = z;
                        }
                        if (z >= mesh.getMaxExtends(currentModel).z) {
                            mesh.getMaxExtends(currentModel).z = z;
                        }
                    }

                    mesh.putPositions(new float[] { //
                            positions[tri.getPosIndex(0)].x, //
                            positions[tri.getPosIndex(0)].y, //
                            positions[tri.getPosIndex(0)].z, //
                            positions[tri.getPosIndex(1)].x, //
                            positions[tri.getPosIndex(1)].y, //
                            positions[tri.getPosIndex(1)].z, //
                            positions[tri.getPosIndex(2)].x, //
                            positions[tri.getPosIndex(2)].y, //
                            positions[tri.getPosIndex(2)].z, //
                    });
                    if (texCoordCount > 0) {
                        mesh.putTexCoords(new float[] { //
                                texCoords[tri.getTexIndex(0)].x, //
                                texCoords[tri.getTexIndex(0)].y, //
                                texCoords[tri.getTexIndex(1)].x, //
                                texCoords[tri.getTexIndex(1)].y, //
                                texCoords[tri.getTexIndex(2)].x, //
                                texCoords[tri.getTexIndex(2)].y, //
                        });
                    }
                    mesh.putNormals(new float[] { //
                            normals[tri.getNormIndex(0)].x, //
                            normals[tri.getNormIndex(0)].y, //
                            normals[tri.getNormIndex(0)].z, //
                            normals[tri.getNormIndex(1)].x, //
                            normals[tri.getNormIndex(1)].y, //
                            normals[tri.getNormIndex(1)].z, //
                            normals[tri.getNormIndex(2)].x, //
                            normals[tri.getNormIndex(2)].y, //
                            normals[tri.getNormIndex(2)].z, //
                    });
                }
            }
        }

        mesh.flipBuffers();
        mesh.prepare();

        Resources.meshes.put(type, mesh);

        return mesh;
    }
    
    public static ArrayList<Entity> loadLevel(PrimitiveType type) {
        ArrayList <Entity> objects = new ArrayList<>();
        
        
        
        return objects;
    }

    private static Tri[] parseFace(String[] blocks) {
        // v=4 => 2 triangles (tri1: v0, v1, v2; tri2: v0, v2, v3)...
        Tri[] tris = new Tri[blocks.length - 2];
        for (int i = 0; i < tris.length; i++) {
            tris[i] = new Tri();
            for (int j = 0; j < 3; j++) {
                // blender is saving indices as for a triangle strip? hence the -i
                String block = blocks[i + j + (j == 0 ? -i : 0)];
                String[] values = block.split("/");
                int p = Integer.parseInt(values[0]);
                int t = values[1].length() > 0 ? Integer.parseInt(values[1]) : 0;
                int n = Integer.parseInt(values[2]);
                tris[i].addVertexData(p - 1, t - 1, n - 1);
            }
        }

        return tris;
    }

    // NOTE fix rotations: top needs to be rotated by 180°, bottom by 90°
    public static int loadSkyboxTexture(String name) {
        int texture = 0;
        ByteBuffer pixelBuffer = null;
        texture = GL11.glGenTextures();
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
        try {
            String[] names = new String[] { name + "_rt", name + "_lf", name + "_up", name + "_dn", name + "_bk",
                    name + "_ft" };
            for (int i = 0; i < names.length; i++) {
                String fullpath = "/textures/skyboxes/" + name.toLowerCase() + "/" + names[i].toLowerCase() + ".png";
                InputStream is = null;
                if (Misc.EXPORTING) {
                    is = Loader.class.getResourceAsStream(fullpath);
                } else {
                    is = new FileInputStream(new File("res" + fullpath));
                }
                BufferedImage bi = ImageIO.read(is);
                int width = bi.getWidth();
                int height = bi.getHeight();

                if (pixelBuffer == null) {
                    pixelBuffer = BufferUtils.createByteBuffer(width * height * 3);
                }
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int color = bi.getRGB(x, y);
                        // byte a = (byte) ((color >> 24) & 0xff);
                        byte r = (byte) ((color >> 16) & 0xff);
                        byte g = (byte) ((color >> 8) & 0xff);
                        byte b = (byte) ((color >> 0) & 0xff);
                        pixelBuffer.put(new byte[] { r, g, b });
                    }
                }
                pixelBuffer.flip();
                GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGB, 512, 512, 0, GL11.GL_RGB,
                        GL11.GL_UNSIGNED_BYTE, pixelBuffer);
                pixelBuffer.clear();
            }
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture;
    }

    public static Texture loadTexture(TextureType type) {
        if (Resources.textures.containsKey(type)) {
            return Resources.textures.get(type);
        }
        int width = 0, height = 0;
        ByteBuffer pixelBuffer = null;
        String name = type.toString().toLowerCase();
        String fullpath = "/textures/objects/" + name + ".png";
        try {
            BufferedImage bi = null;
            if (Misc.EXPORTING) {
                bi = ImageIO.read(Loader.class.getResourceAsStream(fullpath));
            } else {
                bi = ImageIO.read(new File("res" + fullpath));
            }
            width = bi.getWidth();
            height = bi.getHeight();

            pixelBuffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int color = bi.getRGB(x, height - y - 1);
                    byte a = (byte) ((color >> 24) & 0xff);
                    byte r = (byte) ((color >> 16) & 0xff);
                    byte g = (byte) ((color >> 8) & 0xff);
                    byte b = (byte) ((color >> 0) & 0xff);
                    pixelBuffer.put(new byte[] { r, g, b, a });
                }
            }
            pixelBuffer.flip();
        } catch (IOException e) {
            System.err.println(fullpath);
            e.printStackTrace();
        }

        Texture texture = new Texture(pixelBuffer, width, height);
        texture.prepare();

        Resources.textures.put(type, texture);

        return texture;
    }

    @SuppressWarnings("null")
    public static Shader loadShader(ShaderType type) {
        if (Resources.shaders.containsKey(type)) {
            return Resources.shaders.get(type);
        }
        String name = type.toString().toLowerCase();
        String[] shaderSrcs = new String[] { "", "", "" };
        String fullpath = "/shaders/" + name + ".shader";

        Scanner scanner = null;
        if (Misc.EXPORTING) {
            scanner = new Scanner(Loader.class.getResourceAsStream(fullpath));
        } else {
            try {
                scanner = new Scanner(new FileInputStream(new File("res" + fullpath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        int index = -1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#shader")) {
                if (line.endsWith("vertex")) {
                    index = 0;
                } else if (line.endsWith("geometry")) {
                    index = 1;
                } else if (line.endsWith("fragment")) {
                    index = 2;
                }
            } else {
                shaderSrcs[index] += line + '\n';
            }
        }
        scanner.close();

        Shader shader = new Shader(type);
        shader.compile(shaderSrcs);

        Resources.shaders.put(type, shader);

        return shader;
    }
}
