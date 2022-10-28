package kr.piebin.piegun.manager.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GunTeamManager {
    private static Map<Player, String> teamMap;

    public static void init() {
        teamMap = new HashMap<>();
    }

    public static boolean checkTeam(Player p1, Player p2) {
        if (teamMap == null) return false;
        if (teamMap.containsKey(p1) && teamMap.containsKey(p2)) {
            if (teamMap.get(p1).equals(teamMap.get(p2))) {
                return true;
            }
        }
        return false;
    }

    public static String[] getTeamList() {
        Map<String, Boolean> teamListMap = new HashMap<>();
        teamMap.forEach((Player player, String team) -> teamListMap.put(team, true));

        return teamListMap.keySet().toArray(new String[teamListMap.size()]);
    }

    public static void setTeam(Player player, String team) {
        teamMap.put(player, team);
    }

    public static String getTeam(Player player) {
        if (teamMap == null) return null;
        return teamMap.get(player);
    }

    public static void clearTeam(Player player) {
        if (teamMap.containsKey(player)) {
            teamMap.remove(player);
        }
    }
}
