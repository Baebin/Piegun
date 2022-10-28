package kr.piebin.piegun.listener;

import kr.piebin.piegun.action.GunAuto;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class GunSwapListener implements Listener {
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        String weapon = item.getItemMeta().getDisplayName().toLowerCase();

        if (!GunUtilManager.gunMap.containsKey(weapon)) return;
        if (!item.getType().equals(GunUtilManager.getItem(weapon).getType())) return;

        event.setCancelled(true);

        GunAuto.toggleAuto(player, weapon);
    }
}
