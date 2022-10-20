package kr.piebin.piegun.listener;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.action.GunReload;
import kr.piebin.piegun.action.GunZoom;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GunZoomListener implements Listener {
    @EventHandler
    public void onZoom(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        String weapon = item.getItemMeta().getDisplayName().toLowerCase();

        if (!GunUtilManager.gunMap.containsKey(weapon)) return;
        if (!item.getType().equals(GunUtilManager.getItem(weapon).getType())) return;

        event.setCancelled(true);

        if (GunFireManager.getStatus(player).getReloadStatus(weapon)) {
            GunZoom.setZoom(player, item, weapon, false);
            return;
        }

        GunZoom.toggleZoom(player, item, weapon);
    }
}
