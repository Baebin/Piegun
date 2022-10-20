package kr.piebin.piegun.manager;

import kr.piebin.piegun.model.GunStatus;
import org.bukkit.entity.Player;

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
    }

    public static void checkAndInit(Player player) {
        if (!statusMap.containsKey(player))
            statusMap.put(player, new GunStatus());
    }
}
