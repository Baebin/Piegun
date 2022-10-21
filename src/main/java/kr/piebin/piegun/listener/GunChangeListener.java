package kr.piebin.piegun.listener;

import kr.piebin.piegun.action.GunZoom;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.manager.PotionManager;
import kr.piebin.piegun.model.Gun;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class GunChangeListener implements Listener {
    @EventHandler
    public void onChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        ItemStack item_pre = player.getInventory().getItem(event.getPreviousSlot());

        PotionManager.removeSlow(player);
        PotionManager.removeFastDigging(player);

        ItemStack item_helmet = player.getInventory().getHelmet();
        if (item_helmet != null && item_helmet.getType() != Material.AIR) {
            if (GunUtilManager.checkPumpkinItem(item_helmet)) {
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
        }

        if (item_pre != null && item_pre.getType() != Material.AIR) {
            String weapon = item_pre.getItemMeta().getDisplayName().toLowerCase();

            if (GunUtilManager.gunMap.containsKey(weapon)) {
                if (GunFireManager.getStatus(player).getZoomStatus(weapon)) {
                    player.getInventory().setItem(event.getPreviousSlot(), new GunZoom(player, item_pre, weapon).setZoom(false).getItem());
                }
            }
        }

        if (item != null && item.getType() != Material.AIR) {
            String weapon = item.getItemMeta().getDisplayName().toLowerCase();

            if (GunUtilManager.gunMap.containsKey(weapon)) {
                Gun gun = GunUtilManager.gunMap.get(weapon);

                if (item.getType().equals(GunUtilManager.getItem(weapon).getType())) {
                    PacketManager.showActionBar(player, weapon, item);

                    PotionManager.addFastDigging(player);

                    if (item.getAmount() > 1) {
                        item.setAmount(1);
                        player.getInventory().setItem(event.getNewSlot(), item);
                    }

                    if (!gun.isAuto() || !GunFireManager.getStatus(player).getAutoStatus(weapon)) {
                        player.getInventory().setItemInOffHand(GunUtilManager.getShieldItem());
                        return;
                    }
                }
            }
        }

        if (GunUtilManager.checkShieldItem(player.getInventory().getItemInOffHand())) {
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
    }
}
