package kr.piebin.piegun.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunAutoImageManager {
    /*
    private static final int slot = 7;

    private static final int model_auto = 1;
    private static final int model_semi = 2;
    private static final int model_background = 3;

    public static void change(Player player, String weapon) {
        boolean isAutoEnabled = GunFireManager.getStatus(player).getAutoStatus(weapon);
        int model = (isAutoEnabled) ? model_auto : model_semi;

        sendAutoImage(player, model);

    }

    private static void sendAutoImage(Player player, int model) {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        meta.setCustomModelData(model);
        item.setItemMeta(meta);

        //((CraftPlayer)player).getHandle().playerConnection.
        //        sendPacket(new PacketPlayOutSetSlot(0, 36 + 7, CraftItemStack.asNMSCopy(item)));

        player.getInventory().setItem(slot, item);
    }
     */
}