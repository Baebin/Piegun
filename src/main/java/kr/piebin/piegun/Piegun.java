package kr.piebin.piegun;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import kr.piebin.piegun.cmd.CmdPiegun;
import kr.piebin.piegun.database.ConfigManager;
import kr.piebin.piegun.listener.*;
import kr.piebin.piegun.manager.weapon.GunFireManager;
import kr.piebin.piegun.manager.util.GunTeamManager;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import kr.piebin.piegun.manager.util.PacketManager;
import kr.piebin.piegun.skript.effect.GunCacheClear;
import kr.piebin.piegun.skript.effect.GunTeamClear;
import kr.piebin.piegun.skript.effect.GunTeamSet;
import kr.piebin.piegun.skript.expression.GunItem;
import kr.piebin.piegun.skript.expression.GunTeamCheck;
import kr.piebin.piegun.skript.expression.GunTeamGet;
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

        GunTeamManager.init();

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
        Skript.registerEffect(GunCacheClear.class, new String[] { "(clear|clean|delete) gun cache of %player%" });
        Skript.registerEffect(GunTeamClear.class, new String[] { "(clear|delete) gun team of %player%" });
        Skript.registerEffect(GunTeamSet.class, new String[] { "(set|change) gun team of %player% to %string%" });

        Skript.registerExpression(GunItem.class, ItemStack.class, ExpressionType.PROPERTY, new String[] { "gun %string%" });
        Skript.registerExpression(GunTeamCheck.class, Boolean.class, ExpressionType.PROPERTY, new String[] { "gun team check %player% and %player%" });
        Skript.registerExpression(GunTeamGet.class, String.class, ExpressionType.PROPERTY, new String[] { "%player%'s gun team", "gun team of %player%" });

        log("Skript Syntax Added.");
    }
}