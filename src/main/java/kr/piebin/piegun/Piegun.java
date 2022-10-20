package kr.piebin.piegun;

import kr.piebin.piegun.cmd.CmdPiegun;
import kr.piebin.piegun.db.ConfigManager;
import kr.piebin.piegun.listener.GunFireListener;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class Piegun extends JavaPlugin {
    public static final String TAG = "§c[ §ePiegun §c]§f ";

    private static Piegun instance = null;
    public static Piegun getInstance() { return instance; }

    private static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        logger = getServer().getLogger();

        ConfigManager.PATH = getDataFolder().getAbsolutePath() + "\\weapon";

        getCommand("Piegun").setExecutor(new CmdPiegun());
        Bukkit.getPluginManager().registerEvents(new GunFireListener(), this);

        GunManager.loadWeapons();
        try {
            GunManager.createForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GunFireManager.clear();

        log("Piegun Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        log("Piegun Plugin Disabled.");
    }

    public static void log(String message) {
        logger.info(message);
    }
}