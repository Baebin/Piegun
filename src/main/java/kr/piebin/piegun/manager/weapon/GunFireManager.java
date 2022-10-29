package kr.piebin.piegun.manager.weapon;

import kr.piebin.piegun.manager.util.PotionManager;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GunFireManager {
    public static Map<Player, GunStatus> statusMap;

    public static GunStatus getStatus(Player player) {
        checkAndInit(player);
        return statusMap.get(player);
    }

    public static void saveStatus(Player player, GunStatus status) {
        statusMap.put(player, status);
    }

    public static void clear() {
        statusMap = new HashMap<>();
    }

    public static void clear(Player player) {
        statusMap.put(player, new GunStatus());

        ItemStack item_helmet = player.getInventory().getHelmet();
        if (item_helmet != null && item_helmet.getType() != Material.AIR) {
            if (GunUtilManager.checkPumpkinItem(player.getInventory().getHelmet())) {
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
        }

        PotionManager.removeSlow(player);
        PotionManager.removeDigging(player);
    }

    public static void checkAndInit(Player player) {
        if (!statusMap.containsKey(player))
            statusMap.put(player, new GunStatus());
    }
}
