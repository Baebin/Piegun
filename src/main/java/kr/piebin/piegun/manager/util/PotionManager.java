package kr.piebin.piegun.manager.util;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionManager {
    public static final int POTION_TIME = 999999999;

    public static void addSlow(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, POTION_TIME, 1));
    }

    public static void addSlowSniper(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, POTION_TIME, 51));
    }

    public static void removeSlow(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
    }

    public static void addFastDigging(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, POTION_TIME, 51));
    }

    public static void removeFastDigging(Player player) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
