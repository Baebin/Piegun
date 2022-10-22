package kr.piebin.piegun.action;

import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.manager.SoundManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GunAuto {
    public static void toggleAuto(Player player, String weapon) {
        GunStatus status = GunFireManager.getStatus(player);
        setAuto(player, weapon, !status.getAutoStatus(weapon));
    }

    public static void setAuto(Player player, String weapon, boolean mode) {
        Gun gun = GunUtilManager.gunMap.get(weapon);
        if (!gun.isAuto()) return;
        if (mode) {
            ItemStack item = player.getInventory().getItemInOffHand();
            if (item != null && item.getType() != Material.AIR) {
                if (GunUtilManager.checkShieldItem(item)) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                }
            }
        } else player.getInventory().setItemInOffHand(GunUtilManager.getShieldItem());

        GunStatus status = GunFireManager.getStatus(player);
        status.setAutoStatus(weapon, mode);

        showMode(player, weapon);
        SoundManager.playAutoChangedSound(player, gun);
    }

    private static void showMode(Player player, String weapon) {
        PacketManager.sendActionBar(player, "§9Auto: " + GunFireManager.getStatus(player).getAutoStatus(weapon));
    }
}
