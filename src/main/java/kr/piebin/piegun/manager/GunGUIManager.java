package kr.piebin.piegun.manager;

import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.entity.Player;

public class GunGUIManager {
    /*
    # EFB1 : Auto Background
    # EFB2 : Semi Background

    # EFF0 : Big Number (Empty)
    # EFF1 : Small Number (Empty)

    EF01 ~ EF09 : Big Number
    EF11 ~ EF19 : Big Number (Red)
    EF21 ~ EF29 : Big Number (Orange)

    EF51 ~ EF59 : Small Number

    \uF82B\uF82A
    ( \uEFB1 | \uEFB2 )
    \uF80a
    ( \uEF00 & \uEF00 & \uEF00 )
    \uF826\uF801
    ( \uEF50 & \uEF50 & \uEF50 )
    */

    public static void sendGUI(Player player, String weapon, Gun gun) {
        GunStatus status = GunFireManager.getStatus(player);

        int ammo = status.getAmmo(weapon);
        int ammo_max = gun.getAmmo();

        // Init
        String gui = "\\uF82B\\uF82A";

        // Background
        gui += "\\uF826";

        if (status.getAutoStatus(weapon)) {
            gui += "\\uEFB1";
        } else {
            gui += "\\uEFB2";
        }

        // Big Number
        gui += "\\uF80a";

        if (ammo >= 100) {
            gui += "\\uEF0" + (ammo/100) % 10;
        } else {
            gui += "\\uEFF0";
        }

        if (ammo >= 10) {
            gui += "\\uEF0" + (ammo/10) % 10;
        } else if (ammo >= 100){
            gui += "\\uEF00";
        } else {
            gui += "\\uEFF0";
        }

        if (ammo >= 1) {
            gui += "\\uEF0" + (ammo) % 10;
        } else if (ammo >= 10){
            gui += "§c\\uEF00";
        } else {
            gui += "§c\\uEF00";
        }

        // Small Number
        gui += "\\uF826\\uF801";

        if (ammo_max >= 100) {
            gui += "\\uEF5" + (ammo_max/100) % 10;
        } else {
            gui += "\\uEFF1";
        }

        if (ammo_max >= 10) {
            gui += "\\uEF5" + (ammo_max/10) % 10;
        } else if (ammo_max >= 100){
            gui += "\\uEF50";
        } else {
            gui += "\\uEFF1";
        }

        if (ammo_max >= 1) {
            gui += "\\uEF5" + (ammo_max) % 10;
        } else if (ammo_max >= 10){
            gui += "\\uEF50";
        } else {
            gui += "\\uEFF1";
        }

        PacketManager.sendActionBar(player, StringEscapeUtils.unescapeJava(gui));
    }

    private static void getColor(int max_ammo) {

    }
}