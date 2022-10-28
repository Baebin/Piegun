package kr.piebin.piegun.listener;

import kr.piebin.piegun.manager.weapon.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class PumpkinClickListener implements Listener {
    @EventHandler
    public void onPumpkinClick(InventoryClickEvent event) {
        ItemStack item = event.getWhoClicked().getInventory().getHelmet();
        if (item == null || item.getType() == Material.AIR) return;

        if (GunUtilManager.checkPumpkinItem(item)) {
            event.setCancelled(true);
        }
    }
}
