package xtr.uhc.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import xtr.uhc.Core;
import xtr.uhc.Util.ItemStackSerializer;
import xtr.uhc.Util.Utilities;

import java.util.*;

public class ArenaData {


     private Core core = Core.instance;
     private Utilities util = core.getUtil();
     protected static List<Location> locations = new ArrayList<>();
     protected static Map<Object, ArrayList<ItemStack>> kits = new HashMap<>();
     protected String selectedKit = core.getConfig().getString("Arena.SelectedKit");

     public void load(){
         loadArenaKits();
         loadArenaLocations();
         validateKit();
     }

     private void loadArenaKits () {
         for (Object kit : core.getConfig().getStringList("Arena.Kits")) {
             ArrayList<ItemStack> tmpList = new ArrayList<>();
             for (Object item : core.getConfig().getStringList("Arena.Kits." + kit + ".Items")) {
                 tmpList.add(ItemStackSerializer.deserialize(item.toString()));
                 kits.put(kit, tmpList);
             }
         }
     }

     private void loadArenaLocations () {
         for (Object obj : core.getConfig().getStringList("Arena.Locations")) {
             Location loc = util.getLocationString(String.valueOf(obj));
             Bukkit.getConsoleSender().sendMessage(loc.toString());
             locations.add(loc);
         }
     }

     private void validateKit () throws IllegalArgumentException {
         boolean found = false;
         for (Object obj : core.getConfig().getStringList("Arena.Kits").toArray()) {
             if (selectedKit.equals(String.valueOf(obj)))
                 found = true;
             if (found == false)
                 throw new IllegalArgumentException();
         }
     }
}