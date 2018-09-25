package game.inventory;

import org.joml.Math;

public class Ammo {
    private int magazine, magazineCapacity, total, totalCapacity;

    public Ammo() {
        this(0, 0, 0, 0);
    }

    public Ammo(int magazine, int magazineCapacity, int total, int totalCapacity) {
        this.magazine = magazine;
        this.magazineCapacity = magazineCapacity;
        this.total = total;
        this.totalCapacity = totalCapacity;
    }

    public void set(int magazine, int magazineCapacity, int total, int totalCapacity) {
        this.magazine = magazine;
        this.magazineCapacity = magazineCapacity;
        this.total = total;
        this.totalCapacity = totalCapacity;

    }

    public int getMagazine() {
        return magazine;
    }

    public int getTotal() {
        return total;
    }

    public void take(int count) {
        magazine = Math.max(0, magazine - count);
        changed = true;
    }

    public void put(int count) {
        total = Math.min(totalCapacity, total + count);
        changed = true;
    }

    @Override
    public String toString() {
        return "" + magazine + "/" + total;
    }

    public void reload() {
        int delta = Math.min(total, magazineCapacity - magazine);
        total -= delta;
        magazine += delta;
        changed = true;

        //
        magazine += 20;
    }

    public boolean isMagazineEmpty() {
        return magazine == 0;
    }

    // temp
    private boolean changed = true;

    public boolean hasChanged() {
        if (changed) {
            changed = false;
            return true;
        }
        return false;
    }
}
