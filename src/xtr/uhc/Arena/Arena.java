package xtr.uhc.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import xtr.uhc.Core;
import org.bukkit.entity.Player;
import xtr.uhc.Enums.PlayerState;
import xtr.uhc.Manager.UHC;
import xtr.uhc.Manager.UHCPlayer;
import xtr.uhc.Util.Utilities;

import java.util.*;


public class Arena implements CommandExecutor, TabCompleter, Listener {

    private Core core = Core.instance;
    private ArenaGUI agui = core.getAgui();
    private Utilities util = core.getUtil();
    private UHC uhc = core.getUHC();

    @EventHandler
    private void onExplode(EntityExplodeEvent e){
        List<Block> blocks = e.blockList();
        for (Location loc : ArenaData.locations){
            Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
            for (Block blck : blocks){
                if (blck.getBlockKey() == block.getBlockKey()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
        for (Location loc : ArenaData.locations) {
            Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
            if (e.getBlock().equals(block)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onExtend(BlockPistonExtendEvent e){
        List<Block> blocks = e.getBlocks();
        for (Location loc : ArenaData.locations){
            Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
            for (Block blck : blocks){
                if (blck.getBlockKey() == block.getBlockKey()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onRetract(BlockPistonRetractEvent e){
        List<Block> blocks = e.getBlocks();
        for (Location loc : ArenaData.locations){
          Block block = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
            for (Block blck : blocks){
                if (blck.getBlockKey() == block.getBlockKey()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String lbl, String[] args) {
        ArrayList<String> arguments = null;
        switch (args.length) {
            case 1:
                arguments = new ArrayList<>(Arrays.asList("settings", "locations"));
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
        UHCPlayer player = uhc.getPlayers().get(p.getUniqueId());

        switch (args.length) {
            case 0 -> {
               if (ArenaData.locations.size() > 0) {
                   if (player.getState() == PlayerState.LOBBY) {
                       player.setState(PlayerState.ARENA);
                       scatterArena(p);
                   } else if (player.getState() == PlayerState.ARENA) {
                       player.setState(PlayerState.LOBBY);
                       p.teleport(uhc.lobby_loc);
                   }
               }else{p.sendMessage("§c0 §6Locations to teleport to!");}
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
                      if(loc.getWorld().equals(Bukkit.getWorld(core.getConfig().get("Arena.World").toString()))){
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
                        }else{p.sendMessage("§cWorld is not the same as arena's world!");}
                    }
                }
            }
        }
        return true;
    }

    private void scatterArena(Player p) {
            Random rand = new Random();
            Location loc = ArenaData.locations.get(rand.nextInt(ArenaData.locations.size()));
            p.teleport(loc);
    }
}
