package xtr.uhc.Arena;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import xtr.uhc.Core;
import xtr.uhc.Util.ItemStackSerializer;
import xtr.uhc.Util.Utilities;

import java.util.*;

public class ArenaData {

    public ArenaData() {
        kits.clear();
        locations.clear();
        loadArenaKits();
        loadArenaLocations();
        checkSelectedKit();
    }

    private final static Core core = Core.instance;
    private final static Utilities util = core.util;
    protected static List<Location> locations = new ArrayList<>();
    protected static Map<Object, ArrayList<ItemStack>> kits = new HashMap<>();
    protected static String selectedKit = core.getConfig().getString("Arena.SelectedKit");

    protected List<Location> getLocations() {
        return locations;
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

    private void checkSelectedKit() throws IllegalArgumentException {
        boolean found = false;
        for (Object obj : core.getConfig().getConfigurationSection("Arena.Kits").getKeys(false).toArray()) {
            if (selectedKit.equals(String.valueOf(obj)))
                found = true;
            if (found == false)
                throw new IllegalArgumentException();
        }
    }
}