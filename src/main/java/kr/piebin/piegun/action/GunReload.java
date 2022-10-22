package kr.piebin.piegun.action;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.manager.SoundManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GunReload {
    GunStatus status;

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

        status.setReloadStatus(weapon, true);
        GunFireManager.saveStatus(player, status);

        // 1000ms = 20tick
        player.setCooldown(item.getType(), gun.getDelay_reload()/50);
        SoundManager.playReloadSound(player, gun);

        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            int delay_reload = gun.getDelay_reload();
            long time = System.currentTimeMillis();

            int count = 0, count_last = 0;

            int model_index = -1;
            List<Integer> model_reload = gun.getModel_reload();

            String actionbar = null;
            String[] colors = new String[PacketManager.RELOAD_ACTIONBAR];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = "§f―";
            }

            ItemMeta meta = item.getItemMeta();
            while (System.currentTimeMillis() - time <= delay_reload) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                status = GunFireManager.getStatus(player);

                Bukkit.getScheduler().runTask(Piegun.getInstance(), () -> {
                    checkWeaponAndStop();
                });

                if (!status.getReloadStatus(weapon)) {
                    break;
                }

                if (model_reload.size() > 0 && model_index < model_reload.size()-1) {
                    if (model_index < Math.floor((System.currentTimeMillis() - time)/(delay_reload/model_reload.size()))) {
                        model_index = (int) Math.floor((System.currentTimeMillis() - time)/(delay_reload/model_reload.size()));

                        //PacketManager.sendPacketPlayOutSetSlot(player, weapon, model_reload.get(model_index));
                        meta.setCustomModelData(model_reload.get(model_index));
                        item.setItemMeta(meta);
                        player.setItemInHand(item);
                    }
                }

                if (count_last <= Math.floor((System.currentTimeMillis() - time)/(delay_reload/colors.length))) {
                    for (int i = count_last; i < count; i++) {
                        colors[i] = "§b―";
                    }

                    count_last = count;
                    count = (int) Math.floor((System.currentTimeMillis() - time)/(delay_reload/colors.length));
                }

                actionbar = "";
                for (String color: colors) {
                    actionbar += color;
                }

                PacketManager.sendActionBar(player, actionbar);
            }
            if ((status=GunFireManager.getStatus(player)).getReloadStatus(weapon)) {
                //PacketManager.sendPacketPlayOutSetSlot(player, weapon, gun.getModel_default());
                meta.setCustomModelData(gun.getModel_default());
                item.setItemMeta(meta);
                player.setItemInHand(item);

                status.setReloadStatus(weapon, false);
                status.setAmmo(weapon, gun.getAmmo());
                GunFireManager.saveStatus(player, status);

                PacketManager.sendActionBar(player, actionbar.replaceAll("f", "b"));
            } else {
                SoundManager.stopReloadSound(player, gun);
                PacketManager.sendActionBar(player, "");
            }

            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                if (player.getItemInHand().getType() == item.getType()
                        && player.getItemInHand().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {

                    meta.setCustomModelData(gun.getModel_default());
                    item.setItemMeta(meta);
                    player.setItemInHand(item);
                }
            }

            init();
        });
    }

    private void init() {
        Bukkit.getScheduler().runTask(Piegun.getInstance(), () -> {
            player.setCooldown(item.getType(), 0);
            PacketManager.showActionBar(player, weapon, item);
        });
    }

    private void checkWeaponAndStop() {
        ItemStack item_new = player.getItemInHand();
        if (item_new == null || item_new.getType() == Material.AIR) stopReload();
        if (item_new.getType() != item.getType()) stopReload();
        if (item_new.getItemMeta() == null) stopReload();
        if (!item_new.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) stopReload();
    }

    public void stopReload() {
        GunStatus status = GunFireManager.getStatus(player);
        status.setReloadStatus(weapon, false);
        GunFireManager.saveStatus(player, status);
    }
}
