package kr.piebin.piegun.action;

import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PotionManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunZoom {
    Player player;
    ItemStack item;
    String weapon;

    public GunZoom(Player player, ItemStack item, String weapon) {
        this.player = player;
        this.item = item;
        this.weapon = weapon;
    }

    public ItemStack getItem() {
        return item;
    }

    public GunZoom toggleZoom() {
        GunStatus status = GunFireManager.getStatus(player);
        return setZoom(!status.getZoomStatus(weapon));
    }

    public GunZoom setZoom(boolean mode) {
        Gun gun = GunUtilManager.gunMap.get(weapon);

        if (!gun.isZoomEnabled()) return this;

        GunStatus status = GunFireManager.getStatus(player);
        status.setZoomStatus(weapon, mode);

        ItemMeta meta = item.getItemMeta();

        if (!mode) {
            PotionManager.removeSlow(player);
        }

        if (gun.isZoomMotionEnabled()) {
            if (mode) {
                player.getInventory().setHelmet(GunUtilManager.getPunpkinItem());
                PotionManager.addSlowSniper(player);
            } else {
                ItemStack item_helmet = player.getInventory().getHelmet();
                if (item_helmet != null && item_helmet.getType() != Material.AIR) {
                    if (GunUtilManager.checkPumpkinItem(item_helmet)) {
                        player.getInventory().setHelmet(new ItemStack(Material.AIR));
                    }
                }
            }
            return this;
        }

        if (mode) {
            meta.setCustomModelData(gun.getModel_zoom());
            PotionManager.addSlow(player);
        } else {
            meta.setCustomModelData(gun.getModel_default());
        }

        item.setItemMeta(meta);

        return this;
    }
}
