package kr.piebin.piegun.action;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.manager.SoundManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GunReload {
    private GunStatus status;

    Player player;
    ItemStack item;
    String weapon;

    public GunReload(Player player, ItemStack item, String weapon) {
        this.player = player;
        this.item = item;
        this.weapon = weapon;
    }

    public void reload() {
        reload(player, item, weapon);
    }

    private void reload(Player player, ItemStack item, String weapon) {
        if (player == null || item == null || weapon == null) return;

        Gun gun = GunUtilManager.gunMap.get(weapon);

        status = GunFireManager.getStatus(player);
        if (status.getAmmo(weapon) >= gun.getAmmo() || status.getReloadStatus(weapon)) {
            return;
        }

        // 1000ms = 20tick
        player.setCooldown(item.getType(), gun.getDelay_reload()/50);
        SoundManager.playReloadSound(player, gun);

        status.setReloadStatus(weapon, true);
        GunFireManager.saveStatus(player, status);

        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            int delay_reload = gun.getDelay_reload();
            long time = System.currentTimeMillis();

            int count = 0, count_last = 0;
            String[] colors = new String[PacketManager.RELOAD_ACTIONBAR];
            String actionbar;

            for (int i = 0; i < PacketManager.RELOAD_ACTIONBAR; i++) {
                colors[i] = "§f―";
            }

            while (System.currentTimeMillis() - time <= delay_reload) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                status = GunFireManager.getStatus(player);

                Bukkit.getScheduler().runTask(Piegun.getInstance(), () -> {
                    checkWeaponAndStop(player, item, weapon);
                });

                if (!status.getReloadStatus(weapon)) {
                    break;
                };

                if (count_last <= Math.floor((System.currentTimeMillis() - time)/(delay_reload/PacketManager.RELOAD_ACTIONBAR))) {
                    for (int i = count_last; i <= count; i++) {
                        colors[i] = "§b―";
                    }

                    count_last = count;
                    count = (int) Math.floor((System.currentTimeMillis() - time)/(delay_reload/PacketManager.RELOAD_ACTIONBAR));
                }

                actionbar = "";
                for (String color: colors) {
                    actionbar += color;
                }

                PacketManager.sendActionBar(player, actionbar);
            }

            if ((status=GunFireManager.getStatus(player)).getReloadStatus(weapon)) {
                status.setReloadStatus(weapon, false);
                status.setAmmo(weapon, gun.getAmmo());
                GunFireManager.saveStatus(player, status);
            } else {
                SoundManager.stopReloadSound(player, gun);
            }

            Bukkit.getScheduler().runTask(Piegun.getInstance(), () -> {
                player.setCooldown(item.getType(), 0);
                PacketManager.sendActionBar(player, "");
                PacketManager.showActionBar(player, weapon, item);
            });
        });
    }

    private void checkWeaponAndStop(Player player, ItemStack item_weapon, String weapon) {
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) stopReload(player, weapon);
        if (item.getType() != item_weapon.getType()) stopReload(player, weapon);
        if (!item.getItemMeta().getDisplayName().equals(item_weapon.getItemMeta().getDisplayName())) stopReload(player, weapon);
    }

    private void stopReload(Player player, String weapon) {
        GunStatus status = GunFireManager.getStatus(player);
        status.setReloadStatus(weapon, false);
        GunFireManager.saveStatus(player, status);
    }
}