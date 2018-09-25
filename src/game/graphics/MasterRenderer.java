package game.graphics;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import game.World;
import game.entities.DynamicObject;
import game.entities.Entity;
import game.entities.StaticObject;
import game.entities.objects.Projectile;
import game.entities.units.Player;
import game.entities.units.Unit;
import game.graphics.particles.ParticleSystem;
import game.graphics.text.Text;
import game.inventory.weapons.Weapon;
import game.utils.AABB;

public abstract class MasterRenderer {

    public static void draw() {
        ArrayList<Entity> ents = new ArrayList<Entity>();
        ents.addAll(World.staticObjects);
        ents.addAll(World.dynamicObjects);
        ents.addAll(World.units);
        ents.addAll(World.projectiles);
        drawEntities(ents);

        Text.draw();
        HUD.draw();
        ParticleSystem.draw();

    }

    public static void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    private static void drawEntities(ArrayList<Entity> entities) {
        for (Entity e: entities) {
            int polyMode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
            e.prepareForDrawing();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, e.getModel().getMesh().getIndices().capacity());
            e.getModel().unbind();

            if (World.showingBoundingBoxes) {
                for (AABB bb: e.getBoundingBoxes()) {
                    bb.draw();
                }
            }
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, polyMode);
        }
        Weapon weapon = Player.instance.getInventory().getCurrentWeapon();
        weapon.prepareForDrawing();
        // Window.disableDepthTest();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, weapon.getModel().getMesh().getIndices().capacity());
        // Window.enableDepthTest();
        weapon.getModel().unbind();
    }
}
