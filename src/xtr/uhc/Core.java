package xtr.uhc;

import com.samjakob.spigui.SpiGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xtr.uhc.Arena.Arena;
import xtr.uhc.Arena.ArenaData;
import xtr.uhc.Arena.ArenaGUI;
import xtr.uhc.Listeners.GenericListeners;
import xtr.uhc.Manager.UHC;
import xtr.uhc.Manager.UHCPlayer;
import xtr.uhc.Util.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public final class Core extends JavaPlugin implements CommandExecutor {

    public static Core instance;

    private SpiGUI gui;

    public SpiGUI getGUI() {
        return gui;
    }

    private Utilities util;

    public Utilities getUtil() {
        return util;
    }

    private static UHC uhc;

    public UHC getUHC() {
        return uhc;
    }

    private ArenaGUI agui;

    public ArenaGUI getAgui() {
        return agui;
    }


    @Override
    public void onEnable() {

        instance = this;
        createConfig();

        util = new Utilities();
        gui = new SpiGUI(this);
        uhc = new UHC();
        new ArenaData();
        agui = new ArenaGUI();

        getCommand("arena").setExecutor(new Arena());
        getCommand("arena").setTabCompleter(new Arena());

        Bukkit.getPluginManager().registerEvents(new Arena(), this);
        Bukkit.getPluginManager().registerEvents(new GenericListeners(), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            UHCPlayer p = new UHCPlayer(player.getUniqueId());
            uhc.getPlayers().put(player.getUniqueId(), p);
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void createConfig() {
        if (!Files.exists(Path.of(this.getDataFolder() + "/config.yml"))) {
            File file = new File(this.getDataFolder() + "/config.yml");
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                getConfig().addDefault("Worlds.Lobby.World", "UHC");
                getConfig().addDefault("Worlds.Lobby.Spawn", "UHC:48:180:-173");
                getConfig().addDefault("Worlds.UHC", "UHC");
                getConfig().addDefault("Arena.Kits.Test.Name", "Test");
                getConfig().addDefault("Arena.Kits.Test.Icon", "DIAMOND_SWORD");
                getConfig().addDefault("Arena.Kits.Test.Desc", "Lore");
                getConfig().addDefault("Arena.Kits.Test.Items", "DIAMOND_SWORD 1");
                getConfig().addDefault("Arena.World", "UHC");
                getConfig().addDefault("Arena.Max.X", 0);
                getConfig().addDefault("Arena.Max.Y", 0);
                getConfig().addDefault("Arena.Max.Z", 0);
                getConfig().addDefault("Arena.Min.X", 0);
                getConfig().addDefault("Arena.Min.Y", 0);
                getConfig().addDefault("Arena.Min.Z", 0);
                getConfig().addDefault("Arena.Locations", "[]");
                getConfig().addDefault("Arena.SelectedKit", "Test");
                getConfig().addDefault("Arena.Kits.Test.Name", "Test");
                getConfig().addDefault("Arena.Kits.Test.Icon", "DIAMOND_SWORD");
                getConfig().addDefault("Arena.Kits.Test.Desc", "LORE");
                getConfig().addDefault("Arena.Kits.Test.Items", "DIAMOND_SWORD 1 DURABILITY:3 SHARPNESS:5 name:TEST lore:Lore_1|Lore_2");
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;

    }
}