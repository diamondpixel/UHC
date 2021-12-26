package xtr.uhc;

import com.google.gson.Gson;
import com.samjakob.spigui.SpiGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import xtr.uhc.Arena.Arena;
import xtr.uhc.Util.Utilities;


public final class Core extends JavaPlugin implements CommandExecutor {

    public static Core instance;
    public static SpiGUI gui;
    public static TeleportationHandler teleport;
    public static Utilities util;
    public static Arena arena;

    @Override
    public void onEnable() {

        instance = this;
        teleport = new TeleportationHandler();
        util = new Utilities();
        arena = new Arena();
        gui = new SpiGUI(this);

        Bukkit.getPluginManager().registerEvents(new TeleportationHandler(), this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}