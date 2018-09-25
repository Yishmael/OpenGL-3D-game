package game.inventory;

import java.util.ArrayList;

import game.inventory.weapons.Weapon;
import game.time.Timer;

public class Inventory {
    ArrayList<Weapon> weapons = new ArrayList<>();
    private int currentWeaponIndex = -1;

    // ? TODO make inventory static

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        System.out.println(weapon);
        currentWeaponIndex = weapons.size() - 1;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    float lastTimeWeaponSwitched;

    public void nextWeapon() {
        if (Timer.now() - lastTimeWeaponSwitched > 0.5f) {
            lastTimeWeaponSwitched = Timer.now();

            int index = currentWeaponIndex + 1;
            index %= weapons.size();

            currentWeaponIndex = index;
        }
    }

    public void updateCurrentWeapon() {
        if (weapons.size() > 0) {
            weapons.get(currentWeaponIndex).update();
        }
    }

    public Weapon getCurrentWeapon() {
        if (weapons.size() == 0) {
            return null;
        }
        return weapons.get(currentWeaponIndex);
    }

    public void previousWeapon() {
        if (Timer.now() - lastTimeWeaponSwitched > 0.5f) {
            lastTimeWeaponSwitched = Timer.now();
            
            int index = currentWeaponIndex - 1;
            if (index < 0) {
                index = weapons.size() - 1;
            }
            currentWeaponIndex = index;
        }
    }

}
