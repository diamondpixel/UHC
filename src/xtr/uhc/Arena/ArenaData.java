package xtr.uhc.Arena;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import xtr.uhc.Core;
import xtr.uhc.Util.ItemStackSerializer;
import xtr.uhc.Util.Utilities;

import java.util.*;

public class ArenaData {


    private static Core core = Core.instance;
    private static Utilities util = core.getUtil();
    protected static List<Location> locations = new ArrayList<>();
    protected static Map<Object, ArrayList<ItemStack>> kits = new HashMap<>();
    protected static String selectedKit = core.getConfig().getString("Arena.SelectedKit");
    protected static Location MaxLoc = util.getLocationString(core.getConfig().getString("Arena.Max"));
    protected static Location MinLoc= util.getLocationString(core.getConfig().getString("Arena.Min"));

    public ArenaData() {
        loadArenaKits();
        loadArenaLocations();
    }

    public static Location getMaxLoc(){
        return MaxLoc;
    }

    public static Location getMinLoc(){
        return MinLoc;
    }

    private void loadArenaKits() {
        for (Object kit : core.getConfig().getConfigurationSection("Arena.Kits").getKeys(false).toArray()) {
            ArrayList<ItemStack> tmpList = new ArrayList<>();
            for (Object item : core.getConfig().getStringList("Arena.Kits." + kit + ".Items")) {
                tmpList.add(ItemStackSerializer.deserialize(item.toString()));
                kits.put(kit, tmpList);
            }
        }
    }

    private void loadArenaLocations() {
        for (Object obj : core.getConfig().getStringList("Arena.Locations")) {
            Location loc = util.getLocationString(String.valueOf(obj));
            locations.add(loc);
        }
    }
}