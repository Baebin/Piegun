package kr.piebin.piegun.listener;

import kr.piebin.piegun.action.GunReload;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class GunReloadListener implements Listener {
    @EventHandler
    public void onReload(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null || item.getType() == Material.AIR) return;

        String weapon = item.getItemMeta().getDisplayName().toLowerCase();

        if (!GunUtilManager.gunMap.containsKey(weapon)) return;
        if (!item.getType().equals(GunUtilManager.getItem(weapon).getType())) return;

        event.setCancelled(true);

        new GunReload(event.getPlayer(), item, weapon).reload();
    }
}
