package kr.piebin.piegun.listener;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunFireListener implements Listener {
    @EventHandler
    public void onFire(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();
            String weapon = item.getItemMeta().getDisplayName().toLowerCase();

            if (!GunManager.gunMap.containsKey(weapon)) return;
            Gun gun = GunManager.gunMap.get(weapon);

            if (!item.getType().equals(GunManager.getItem(weapon).getType())) return;

            event.setCancelled(true);

            Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
                GunStatus status = GunFireManager.getStatus(player);

                if (!status.getActionBarStatus(weapon)) showActionBar(player, weapon, item);

                if (status.getFireStatus(weapon)) {
                    return;
                }

                if (System.currentTimeMillis() - status.getFireTime(weapon) < gun.getDelay_fire()) {
                    return;
                }

                status.setFireStatus(weapon, true);
                GunFireManager.saveStatus(player, status);

                int delay = 220;
                int delay_fire = gun.getDelay_fire();
                long start_time = System.currentTimeMillis();

                int ammo;
                do {
                    status = GunFireManager.getStatus(player);

                    if (System.currentTimeMillis() - status.getFireTime(weapon) >= delay_fire) {
                        status.setFireTime(weapon, System.currentTimeMillis());

                        ammo = status.getAmmo(weapon);
                        if (ammo > 0) {
                            status.setAmmo(weapon, --ammo);
                            playSound(player, gun.getSound_shoot());

                            showBullet(player, weapon);
                        } else {
                            playSound(player, gun.getSound_empty());
                        }

                        GunFireManager.saveStatus(player, status);

                        if (delay - (System.currentTimeMillis() - start_time) - delay_fire <= 0) {
                            break;
                        } else {
                            try {
                                Thread.sleep(delay_fire);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!gun.isAuto()) break;
                        }
                    }
                } while (System.currentTimeMillis() - start_time <= delay);

                status.setFireStatus(weapon, false);
                GunFireManager.saveStatus(player, status);
            });
        }
    }

    @EventHandler
    public void onPlayerItemChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item != null && item.getType() != Material.AIR) {
            String weapon = item.getItemMeta().getDisplayName().toLowerCase();

            if (GunManager.gunMap.containsKey(weapon)) {
                Gun gun = GunManager.gunMap.get(weapon);

                if (item.getType().equals(GunManager.getItem(weapon).getType())) {
                    showActionBar(player, weapon, item);

                    if (item.getAmount() > 1) {
                        item.setAmount(1);
                        player.getInventory().setItem(event.getNewSlot(), item);
                    }

                    if (!gun.isAuto()) {
                        player.getInventory().setItemInOffHand(GunManager.getShieldItem());
                        return;
                    }
                }
            }
        }

        if (GunManager.checkShieldItem(player.getInventory().getItemInOffHand())) {
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
    }

    private void playSound(Player player, String sound) {
        player.getWorld().playSound(player.getLocation(), sound, 1, 1);
    }

    private void showActionBar(Player player, String weapon, ItemStack item_pre) {
        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            ItemStack item_new;

            if (item_pre == null || item_pre.getType() == Material.AIR) {
                return;
            }

            GunStatus status = GunFireManager.getStatus(player);
            if (status.getActionBarStatus(weapon)) return;

            status.setActionBarStatus(weapon, true);
            GunFireManager.saveStatus(player, status);

            ItemMeta meta_pre = item_pre.getItemMeta();
            ItemMeta meta_new;

            meta_pre.setCustomModelData(0);

            Gun gun = GunManager.gunMap.get(weapon);

            int delay_fire = gun.getDelay_fire();
            if (delay_fire <= 100) delay_fire = 100;

            while (player.isOnline()) {
                try {
                    Thread.sleep(delay_fire);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                item_new = player.getItemInHand();

                if (item_new == null || item_new.getType() == Material.AIR) {
                    break;
                }

                meta_new = item_new.getItemMeta();
                meta_new.setCustomModelData(0);

                if (!meta_pre.getDisplayName().equals(meta_new.getDisplayName()) || item_pre.getType() != item_new.getType()) {
                    break;
                }

                showBullet(player, weapon);
            }

            status = GunFireManager.getStatus(player);
            status.setActionBarStatus(weapon, false);
            GunFireManager.saveStatus(player, status);
        });
    }

    private void showBullet(Player player, String weapon) {
        Gun gun = GunManager.gunMap.get(weapon);
        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                "§c" + GunFireManager.getStatus(player).getAmmo(weapon) + " / " + gun.getAmmo()));
    }
}
