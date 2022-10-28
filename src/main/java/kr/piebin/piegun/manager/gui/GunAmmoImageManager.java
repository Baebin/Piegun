package kr.piebin.piegun.manager.gui;

import kr.piebin.piegun.model.Gun;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunAmmoImageManager {
    /*
    private static final int slot = 8;

    private static final int model_default = 3;

    public static void change(Player player, String weapon, Gun gun) {
        int ammo = GunFireManager.getStatus(player).getAmmo(weapon);
        int model = model_default;

        if (gun.getModel_ammo().size() > ammo)
            model = gun.getModel_ammo().get(ammo);

        sendAmmoImage(player, model);

    }

    private static void sendAmmoImage(Player player, int model) {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        meta.setCustomModelData(model);
        item.setItemMeta(meta);

        //((CraftPlayer)player).getHandle().playerConnection.
        //        sendPacket(new PacketPlayOutSetSlot(0, 36 + 8, CraftItemStack.asNMSCopy(item)));

        player.getInventory().setItem(slot, item);
    }
     */
}