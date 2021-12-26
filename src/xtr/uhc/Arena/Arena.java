package xtr.uhc.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import xtr.uhc.Core;
import org.bukkit.entity.Player;
import xtr.uhc.Util.Utilities;

import java.util.*;


public class Arena implements CommandExecutor, TabCompleter, Listener {

    private Core core = Core.instance;
    private Utilities util = Core.util;
    public static ArenaData adata;
    public static ArenaGUI agui;

    public Arena() {
        adata = new ArenaData();
        agui = new ArenaGUI();
        core.getCommand("arena").setExecutor(this);
        core.getCommand("arena").setTabCompleter(this);
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
        for (Location loc : adata.getLocations()) {
            Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
            if (e.getBlock().equals(block)) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String lbl, String[] args) {
        ArrayList<String> arguments = null;
        switch (args.length) {
            case 1:
                arguments = new ArrayList<>(Arrays.asList("settings", "locations", "kits"));
            case 2:
                if (args[0].equalsIgnoreCase("locations")) {
                    arguments = new ArrayList<>(Arrays.asList("add"));
                }
        }
        return arguments;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command command, String lbl, String[] args) {

        if (cs instanceof ConsoleCommandSender) {
            return false;
        }

        Player p = (Player) cs;

        switch (args.length) {
            case 0 -> {
                scatterArena(p);
            }
            case 1 -> {
                if (args[0].equalsIgnoreCase("settings"))
                    agui.arenaGUI(p);
                if (args[0].equalsIgnoreCase("locations"))
                    for (Location loc : adata.getLocations())
                        p.sendMessage(util.locationToString(loc));
                if (args[0].equalsIgnoreCase("kits"))
                    for (Map.Entry<Object, ArrayList<ItemStack>> k : adata.kits.entrySet()) {
                        p.sendMessage(String.valueOf(k.getKey()));
                        for (ItemStack v : k.getValue()) {
                            p.sendMessage(" " + v.toString());
                        }
                    }
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("locations") && args[1].equalsIgnoreCase("add")) {
                    Location loc = p.getLocation();
                    Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
                    if (!block.getType().toString().endsWith("AIR")) {
                        if (!adata.getLocations().contains(loc) && !core.getConfig().getStringList("Arena.Locations").contains(util.locationToString(loc))) {
                            adata.getLocations().add(loc);
                            List<String> list = new ArrayList<>();
                            core.getConfig().getStringList("Arena.Locations").forEach((location) -> {
                                list.add(location);
                            });
                            list.add(util.locationToString(loc));
                            core.getConfig().set("Arena.Locations", list);
                            core.saveConfig();
                        }
                    }
                }
            }
        }
        return true;
    }

    private void scatterArena(Player p) {
        try {
            Random rand = new Random();
            Location loc = adata.getLocations().get(rand.nextInt(adata.getLocations().size()));
            p.teleport(loc);
        } catch (IllegalArgumentException e) {
            p.sendMessage("0 Locations to teleport to!");
        }
    }
}
