package kr.piebin.piegun.manager;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PacketManager {
    public static boolean ISSTOPPED = false;
    public static final int RELOAD_ACTIONBAR = 10;

    public static void sendPacketPlayOutPosition(Player player, float yaw, Float pitch) {
        Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> flagsSet = new HashSet<>(
                Arrays.asList(
                        PacketPlayOutPosition.EnumPlayerTeleportFlags.X,
                        PacketPlayOutPosition.EnumPlayerTeleportFlags.Y,
                        PacketPlayOutPosition.EnumPlayerTeleportFlags.Z
                )
        );

        PacketPlayOutPosition packet = new PacketPlayOutPosition(0.0, 0.0, 0.0, yaw, pitch, flagsSet, 0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void spawnCrit(Location location) {
        location.getWorld().spawnParticle(
                Particle.CRIT, location.getX(), location.getY(), location.getZ(), 1, 0, 0, 0, 0, null);
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void showActionBar(Player player, String weapon, ItemStack item_pre) {
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

            while (player.isOnline()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (ISSTOPPED) return;
                if (GunFireManager.getStatus(player).getReloadStatus(weapon)) break;

                item_new = player.getItemInHand();

                if (item_new == null || item_new.getType() == Material.AIR) {
                    break;
                }

                meta_new = item_new.getItemMeta();

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

    public static void showBullet(Player player, String weapon) {
        Gun gun = GunUtilManager.gunMap.get(weapon);
        sendActionBar(player, "§c" + GunFireManager.getStatus(player).getAmmo(weapon) + " / " + gun.getAmmo());
    }
}
