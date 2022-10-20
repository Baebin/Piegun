package kr.piebin.piegun.listener;

import kr.piebin.piegun.manager.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class GunSwapListener implements Listener {
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        String weapon = item.getItemMeta().getDisplayName().toLowerCase();

        if (!GunUtilManager.gunMap.containsKey(weapon)) return;
        if (!item.getType().equals(GunUtilManager.getItem(weapon).getType())) return;

        event.setCancelled(true);
    }
}
