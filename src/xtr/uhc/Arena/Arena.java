package xtr.uhc.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import xtr.uhc.Core;
import org.bukkit.entity.Player;
import xtr.uhc.Enums.PlayerState;
import xtr.uhc.Manager.UHC;
import xtr.uhc.Manager.UHCPlayer;
import xtr.uhc.Util.Utilities;

import java.util.*;


public class Arena implements CommandExecutor, TabCompleter, Listener {

    private final Core core = Core.instance;
    private final ArenaGUI agui = core.getAgui();
    private final Utilities util = core.getUtil();
    private final UHC uhc = core.getUHC();

    @EventHandler
    private void onExplode(EntityExplodeEvent e) {
      for (Block block : e.blockList()) {
          if (util.blockWithin(block.getWorld(),block, ArenaData.getMaxLoc(), ArenaData.getMinLoc()))
              e.setCancelled(true);
              break;
         }
        }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
       if (util.blockWithin(e.getBlock().getWorld(),e.getBlock(), ArenaData.getMaxLoc(), ArenaData.getMinLoc()))
          e.setCancelled(true);
    }

    @EventHandler
    private void onPlace(BlockPlaceEvent e) {
        if (util.blockWithin(e.getBlock().getWorld(),e.getBlock(), ArenaData.getMaxLoc(), ArenaData.getMinLoc()))
            e.setCancelled(true);
    }

    @EventHandler
    private void onExtend(BlockPistonExtendEvent e) {
        List<Block> blocks = e.getBlocks();
            for (Block block : blocks) {
                if (util.blockWithin(block.getWorld(),block, ArenaData.getMaxLoc(), ArenaData.getMinLoc()))
                    e.setCancelled(true);
                    break;
            }
        }

    @EventHandler
    private void onRetract(BlockPistonRetractEvent e) {
        List<Block> blocks = e.getBlocks();
        for (Block block : blocks) {
            if (util.blockWithin(block.getWorld(),block, ArenaData.getMaxLoc(), ArenaData.getMinLoc()))
                e.setCancelled(true);
            break;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String lbl, String[] args) {
        ArrayList<String> arguments = null;
        switch (args.length) {
            case 1:
                arguments = new ArrayList<>(Arrays.asList("settings", "locations","kit","max","min"));
            case 2:
                if (args[0].equalsIgnoreCase("locations"))
                    arguments = new ArrayList<>(List.of("add"));
                if (args[0].equalsIgnoreCase("kit") || args[0].equalsIgnoreCase("max") || args[0].equalsIgnoreCase("min"))
                    arguments = new ArrayList<>(List.of("set"));
            case 3:
                if (args[0].equalsIgnoreCase("kit") && args[1].equalsIgnoreCase("set"))
                    arguments = new ArrayList<>(core.getConfig().getConfigurationSection("Arena.Kits").getKeys(false));
        }
        return arguments;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command command, @NotNull String lbl, String[] args) {

        if (cs instanceof ConsoleCommandSender) {
            return false;
        }

        Player p = (Player) cs;
        UHCPlayer player = uhc.getPlayers().get(p.getUniqueId());

        switch (args.length) {
            case 0 -> {
                if (ArenaData.locations.size() > 0) {
                    if (player.getState() == PlayerState.LOBBY) {
                        player.setState(PlayerState.ARENA);
                        scatterArena(p);
                    } else if (player.getState() == PlayerState.ARENA) {
                        player.setState(PlayerState.LOBBY);
                        p.getInventory().clear();
                        p.teleport(uhc.getLobbySpawn());
                    }
                } else {
                    p.sendMessage("§c0 §6Locations to teleport to!");
                }
            }
            case 1 -> {
                if (args[0].equalsIgnoreCase("settings"))
                    agui.arenaGUI(p);
                if (args[0].equalsIgnoreCase("locations"))
                    for (Location loc : ArenaData.locations)
                        p.sendMessage(util.locationToString(loc));
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("locations") && args[1].equalsIgnoreCase("add")) {
                    Location loc = p.getLocation();
                    Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
                    if (!block.getType().toString().endsWith("AIR")) {
                        if (loc.getWorld().equals(Bukkit.getWorld(core.getConfig().get("Arena.World").toString()))) {
                            if (!ArenaData.locations.contains(loc) && !core.getConfig().getStringList("Arena.Locations").contains(util.locationToString(loc))) {
                                ArenaData.locations.add(loc);
                                List<String> list = new ArrayList<>();
                                core.getConfig().getStringList("Arena.Locations").forEach((location) -> {
                                    list.add(location);
                                });
                                list.add(util.locationToString(loc));
                                core.getConfig().set("Arena.Locations", list);
                                core.saveConfig();
                            }
                        } else {
                            p.sendMessage("§cWorld is not the same as arena's world!");
                        }
                    }
                }
                switch (args[0].toLowerCase()){
                    case "max":
                        if (args[1].equalsIgnoreCase("set"))
                             ArenaData.MaxLoc = p.getLocation();
                             core.getConfig().set("Arena.Max",util.locationToString(p.getLocation()));
                             core.saveConfig();
                    case "min":
                        if (args[1].equalsIgnoreCase("set"))
                            ArenaData.MinLoc = p.getLocation();
                            core.getConfig().set("Arena.Min",util.locationToString(p.getLocation()));
                            core.saveConfig();
                }
            }
            case 3 ->{
                if (args[0].equalsIgnoreCase("kit") && args[1].equalsIgnoreCase("set")) {
                    for (Map.Entry<Object, ArrayList<ItemStack>> k : ArenaData.kits.entrySet()) {
                       if (k.getKey().equals(args[2]))
                            ArenaData.selectedKit = args[2];
                            core.getConfig().set("Arena.SelectedKit", args[2]);
                            core.saveConfig();
                    }
                }
            }
        }
        return true;
    }

    private void scatterArena(Player p) {
        Random rand = new Random();
        Location loc = ArenaData.locations.get(rand.nextInt(ArenaData.locations.size()));
        for (Map.Entry<Object, ArrayList<ItemStack>> k : ArenaData.kits.entrySet()){
            for (ItemStack item : k.getValue()){
                if (ArenaData.selectedKit.equals(k.getKey())){
                    p.getInventory().addItem(item);
                }
            }
        }
         p.teleport(loc);
    }

    private void equipKit(@NotNull Player p){
        ListIterator iterator = p.getInventory().iterator();
    while(iterator.hasNext()) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (util.chestplates.contains(item.getType())) {
                if (p.getInventory().getHelmet() == null)
                    p.getInventory().setHelmet(item);
            }
            if (util.chestplates.contains(item.getType())) {
                if (p.getInventory().getHelmet() == null)
                    p.getInventory().setHelmet(item);
            }
            if (util.leggings.contains(item.getType())) {
                if (p.getInventory().getHelmet() == null)
                    p.getInventory().setHelmet(item);
            }
            if (util.boots.contains(item.getType())) {
                if (p.getInventory().getHelmet() == null)
                    p.getInventory().setHelmet(item);
            }
            p.getInventory().remove(item);
            p.updateInventory();
        }
      }
    }

}
