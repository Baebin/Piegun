package kr.piebin.piegun;

import kr.piebin.piegun.cmd.CmdPiegun;
import kr.piebin.piegun.db.ConfigManager;
import kr.piebin.piegun.listener.GunChangeListener;
import kr.piebin.piegun.listener.GunFireListener;
import kr.piebin.piegun.listener.GunReloadListener;
import kr.piebin.piegun.listener.GunSwapListener;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
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

        Bukkit.getPluginManager().registerEvents(new GunChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunFireListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunReloadListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunSwapListener(), this);

        GunUtilManager.loadWeapons();
        try {
            GunUtilManager.createForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GunFireManager.clear();

        log("Piegun Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        PacketManager.ISSTOPPED = true;
        GunFireManager.clear();

        log("Piegun Plugin Disabled.");
    }

    public static void log(String message) {
        logger.info(message);
    }
}