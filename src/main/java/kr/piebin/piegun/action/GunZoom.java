package kr.piebin.piegun.action;

import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunZoom {
    public static void toggleZoom(Player player, ItemStack item, String weapon) {
        GunStatus status = GunFireManager.getStatus(player);
        setZoom(player, item, weapon, !status.getZoomStatus(weapon));
    }

    public static void setZoom(Player player, ItemStack item, String weapon, boolean mode) {
        Gun gun = GunUtilManager.gunMap.get(weapon);

        GunStatus status = GunFireManager.getStatus(player);
        status.setZoomStatus(weapon, mode);

        ItemMeta meta = item.getItemMeta();

        if (mode) {
            meta.setCustomModelData(gun.getModel_zoom());
        } else {
            meta.setCustomModelData(gun.getModel_default());
        }

        item.setItemMeta(meta);
        player.setItemInHand(item);
    }
}
