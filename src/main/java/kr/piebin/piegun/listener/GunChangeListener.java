package kr.piebin.piegun.listener;

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

        PotionManager.removeFastDigging(player);

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

                    if (!gun.isAuto()) {
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
