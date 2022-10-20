package kr.piebin.piegun.listener;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.action.GunReload;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.SoundManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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

        new GunReload(event.getPlayer(), item, weapon).stopReload();

        Gun gun = GunUtilManager.gunMap.get(weapon);
        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            GunStatus status = GunFireManager.getStatus(player);
            float yaw_rebound, pitch_rebound;

            if (!status.getActionBarStatus(weapon)) PacketManager.showActionBar(player, weapon, item);

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

                        PacketManager.showBullet(player, weapon);

                        yaw_rebound = gun.getRx();
                        pitch_rebound = gun.getRy();

                        if (status.getZoomStatus(weapon)) {
                            yaw_rebound /= 2;
                            pitch_rebound /= 2;
                        }
                        // 50% : L&R Rebound
                        if (new Random().nextInt(10) < 5) {
                            yaw_rebound *= -1;
                        }

                        yaw_rebound = player.getLocation().getYaw() + yaw_rebound;
                        pitch_rebound = player.getLocation().getPitch() - pitch_rebound;

                        PacketManager.sendPacketPlayOutPosition(player, yaw_rebound, pitch_rebound);

                        SoundManager.playFireSound(player, gun);
                    } else {
                        SoundManager.playEmptySound(player, gun);
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
