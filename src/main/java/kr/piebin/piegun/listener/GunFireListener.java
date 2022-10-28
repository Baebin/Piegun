package kr.piebin.piegun.listener;

import kr.piebin.piegun.action.GunFire;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GunFireListener implements Listener {
    @EventHandler
    public void onFire(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        String weapon = item.getItemMeta().getDisplayName().toLowerCase();

        if (!GunUtilManager.gunMap.containsKey(weapon)) return;
        if (!item.getType().equals(GunUtilManager.getItem(weapon).getType())) return;

        event.setCancelled(true);

        new GunFire(player, item, weapon).fire();
    }
}
