package kr.piebin.piegun.manager.gui;

import kr.piebin.piegun.manager.weapon.GunFireManager;
import kr.piebin.piegun.manager.util.PacketManager;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.entity.Player;

public class GunGUIManager {
    /*
    # EFB1 : Auto Background
    # EFB2 : Semi Background
    # EFB3 : Reload Background

    # EFF0 : Big Number (Empty)
    # EFF1 : Small Number (Empty)

    EF01 ~ EF09 : Big Number
    EF11 ~ EF19 : Big Number (Red)
    EF21 ~ EF29 : Big Number (Orange)

    EF51 ~ EF59 : Small Number

    \uF82B\uF82A
    ( \uEFB1 | \uEFB2 | \uEFB3 )
    \uF80A
    ( \uEF00 & \uEF00 & \uEF00 )
    \uF826\uF801
    ( \uEF50 & \uEF50 & \uEF50 )
    */

    public static void sendGUI(Player player, String weapon) {
        sendGUI(player, weapon, GunUtilManager.gunMap.get(weapon));
    }

    public static void sendGUI(Player player, String weapon, Gun gun) {
        GunStatus status = GunFireManager.getStatus(player);

        if (status.getReloadStatus(weapon)) {
            String gui = "\\uF82B\\uF82A\\uF821" + "\\uEFB3";
            PacketManager.sendActionBar(player, StringEscapeUtils.unescapeJava(gui));
            return;
        }

        int ammo = status.getAmmo(weapon);
        int ammo_max = gun.getAmmo();

        // Init
        String gui = "\\uF82B\\uF82A";

        // Background
        if (status.getAutoStatus(weapon)) {
            gui += "\\uEFB1";
        } else {
            gui += "\\uEFB2";
        }

        // Big Number
        gui += "\\uF80A";

        // Default: 0
        // Red: 1
        // Orange: 2

        int color = 0;
        if ((ammo*100)/ammo_max <= 20) {
            color = 1;
        } else if ((ammo*100)/ammo_max <= 50) {
            color = 2;
        }

        if (ammo >= 100) {
            gui += "\\uEF" + color + (ammo/100) % 10;
        } else {
            gui += "\\uEFF0";
        }

        if (ammo >= 10) {
            gui += "\\uEF" + color + (ammo/10) % 10;
        } else if (ammo >= 100){
            gui += "\\uEF" + color + "0";
        } else {
            gui += "\\uEFF0";
        }

        if (ammo >= 1) {
            gui += "\\uEF" + color + (ammo) % 10;
        } else if (ammo >= 10){
            gui += "\\uEF" + color + "0";
        } else {
            gui += "\\uEF10";
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
}