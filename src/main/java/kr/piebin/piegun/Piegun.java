package kr.piebin.piegun;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import kr.piebin.piegun.cmd.CmdPiegun;
import kr.piebin.piegun.database.ConfigManager;
import kr.piebin.piegun.listener.*;
import kr.piebin.piegun.manager.GunFireManager;
import kr.piebin.piegun.manager.GunUtilManager;
import kr.piebin.piegun.manager.PacketManager;
import kr.piebin.piegun.skript.expression.GunCacheClear;
import kr.piebin.piegun.skript.expression.GunItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
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

        GunUtilManager.loadWeapons();
        try {
            GunUtilManager.createForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GunFireManager.clear();

        registerEvents();
        registerSkript();

        log("Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        PacketManager.ISSTOPPED = true;
        GunFireManager.clear();

        log("Plugin Disabled.");
    }

    public static void log(String message) {
        logger.info(TAG + message);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new GunChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunFireListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunReloadListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunSwapListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunZoomListener(), this);
        Bukkit.getPluginManager().registerEvents(new PumpkinClickListener(), this);
    }

    private void registerSkript() {
        if (Bukkit.getPluginManager().getPlugin("Skript") == null) {
            log("Failed to Add Skript Syntax.");
            return;
        }

        Skript.registerAddon(this);
        Skript.registerEffect(GunCacheClear.class, new String[] { "clear gun cache of %player%" });
        Skript.registerExpression(GunItem.class, ItemStack.class, ExpressionType.PROPERTY, new String[] { "gun of %string%" });
        log("Skript Syntax Added.");
    }
}