package kr.piebin.piegun.manager;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.database.ConfigManager;
import kr.piebin.piegun.model.Gun;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GunUtilManager {
    public static Map<String, Gun> gunMap;
    public static final String FORM = "form.yml";
    public static final String SECRET_NAME = "§b§b";
    public static final int SECRET_CODE = 1000;

    public static ItemStack getItem(String weapon) {
        Gun gun = gunMap.get(weapon);

        ItemStack item = new ItemStack(Material.matchMaterial(gun.getItem()));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(gun.getName());
        itemMeta.setCustomModelData(gun.getModel_default());
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getShieldItem() {
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(SECRET_NAME);
        itemMeta.setCustomModelData(SECRET_CODE);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static boolean checkShieldItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (item.getType() == Material.SHIELD && itemMeta.getCustomModelData() == SECRET_CODE) {
            return true;
        }
        return false;
    }

    public static ItemStack getPunpkinItem() {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(SECRET_NAME);
        itemMeta.setCustomModelData(SECRET_CODE);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static boolean checkPumpkinItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (item.getType() == Material.CARVED_PUMPKIN && itemMeta.getCustomModelData() == SECRET_CODE) {
            return true;
        }
        return false;
    }

    public static void loadWeapons() {
        // init
        gunMap = new HashMap<>();
        ConfigManager.getInstance();

        File files[] = new File(ConfigManager.PATH).listFiles();

        if (files == null) return;
        YamlConfiguration yaml;
        for (File file : files) {
            if (file.getName().equals(FORM) || !(file.getName().substring(file.getName().lastIndexOf(".") + 1)).contains("yml"))
                continue;

            ConfigManager.getInstance().load(file.getName());
            yaml = ConfigManager.getInstance().getYaml(file.getName());

            Gun gun = new Gun();

            gun.setName(yaml.getString("name", ""))
                .setItem(yaml.getString("item", "Ghast_Tear"))

                .setDamage(Float.parseFloat(yaml.getString("damage", "0")))
                .setAmmo(yaml.getInt("ammo", 0))

                .setAuto(yaml.getBoolean("auto", false))

                .setBurst(yaml.getInt("burst", 1))
                .setSpread(yaml.getInt("spread", 1))
                .setDistance(yaml.getInt("distance", 1))

                .setKnockback(Float.parseFloat(yaml.getString("knockback", "0")))

                .setDelay_fire(yaml.getInt("delay_fire", 10))
                .setDelay_reload(yaml.getInt("delay_reload", 0))

                .setRx(Float.parseFloat(yaml.getString("rx", "0")))
                .setRy(Float.parseFloat(yaml.getString("ry", "0")))

                .setParticle(yaml.getString("particle", "CRIT_MAGIC"))

                .setModel_default(yaml.getInt("model_default", 0))
                .setModel_zoom(yaml.getInt("model_zoom", 0))
                .setModel_reload(yaml.getIntegerList("model_reload"))

                .setZoomEnabled(yaml.getBoolean("zoomEnabled", true))
                .setZoomMotionEnabled(yaml.getBoolean("zoomMotionEnabled", false))

                .setSound_shoot(yaml.getString("sound_shoot", ""))
                .setSound_reload(yaml.getString("sound_reload", ""))
                .setSound_empty(yaml.getString("sound_empty", ""))
                .setSound_auto_changed(yaml.getString("sound_auto_changed", "block.note_block.hat"))

                .setSound_hit(yaml.getString("sound_hit", ""))
                .setSound_headshot(yaml.getString("sound_headshot", ""));

            gunMap.put(gun.getName().toLowerCase(), gun);
            Piegun.log("Weapon Loaded - " + gun.getName());
        }
    }

    public static void createForm() throws IOException {
        File file = new ConfigManager().getFile(FORM);
        if (file.exists()) file.delete();

        ConfigManager.checkAndCreateFile(file);

        final InputStream is = Piegun.class.getResourceAsStream("/form.yml");
        if (is == null) return;

        final OutputStream fos = new FileOutputStream(file);
        final byte[] buffer = new byte[4096];

        int len;
        while ((len = is.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }

        is.close();
        fos.close();
    }
}