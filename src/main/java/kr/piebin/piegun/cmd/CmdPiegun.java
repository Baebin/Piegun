package kr.piebin.piegun.cmd;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.model.Gun;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CmdPiegun implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "The console can't execute this command.");
            return false;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            sendMessage(sender, "You don't have permission.");
            return false;
        }

        if (args.length == 0) {
            sendMessage(sender, "/Piegun Get <weapon>");
            sendMessage(sender, "/Piegun Clear <player>");
            sendMessage(sender, "/Piegun List");
            sendMessage(sender, "/Piegun Reload");
            return false;
        }

        if (args[0].equalsIgnoreCase("get")) {
            if (args.length == 1) {
                sendMessage(player, "§7Type the weapon.");
                return false;
            }

            String weapon = args[1];
            for (int i = 2; i < args.length; i++)
                weapon += " " + args[i];

            Map<String, Gun> gunMap = GunUtilManager.gunMap;
            for (String key : gunMap.keySet()) {
                if (key.replaceAll("§\\w", "").equalsIgnoreCase(weapon)) {
                    player.getInventory().addItem(GunUtilManager.getItem(key));
                    return false;
                }
            }

            sendMessage(player, "§7Can't find that weapon.");
        } else if (args[0].equalsIgnoreCase("clear")) {
            GunFireManager.clear();
            sendMessage(player, "§aCache Data Cleared.");
        } else if (args[0].equalsIgnoreCase("list")) {
            Map<String, Gun> gunMap = GunUtilManager.gunMap;
            if (gunMap.size() == 0) {
                sendMessage(player, "§7There is nothing.");
                return false;
            }

            int i = 0;
            for (String key : gunMap.keySet()) {
                sendMessage(player, ++i + ". " + gunMap.get(key).getName());
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            GunUtilManager.loadWeapons();
            sendMessage(player, "§aReloaded.");
        }

        return false;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Piegun.TAG + message);
    }
}