package kr.piebin.piegun.manager;

import kr.piebin.piegun.model.Gun;
import org.bukkit.entity.Player;

public class SoundManager {
    public static void playFireSound(Player player, Gun gun) {
        playSound(player, gun.getSound_shoot());
    }

    public static void playEmptySound(Player player, Gun gun) {
        playSound(player, gun.getSound_empty());
    }

    public static void playReloadSound(Player player, Gun gun) {
        playSound(player, gun.getSound_reload());
    }

    public static void stopReloadSound(Player player, Gun gun) {
        player.stopSound(gun.getSound_reload());
    }

    private static void playSound(Player player, String sound) {
        player.getWorld().playSound(player.getLocation(), sound, 1, 1);
    }
}
