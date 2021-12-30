package xtr.uhc.Arena;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xtr.uhc.Core;
import xtr.uhc.Util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArenaGUI {

    private Core core = Core.instance;
    private Utilities util = core.getUtil();


    protected void arenaGUI(Player player) {
        SGMenu arenaSettings = core.getGUI().create("&6 Arena Settings", 1);
        SGButton kitsButton = new SGButton(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build()).withListener(
                (InventoryClickEvent e) -> {
                    arenaKits((Player) e.getWhoClicked());
                });
        SGButton locationsButton = new SGButton(new ItemBuilder(Material.OAK_SIGN).build()).withListener(
                (InventoryClickEvent e) -> {
                    arenaLocations((Player) e.getWhoClicked());
                });
        arenaSettings.setButton(3, kitsButton);
        arenaSettings.setButton(5, locationsButton);
        arenaSettings.setAutomaticPaginationEnabled(false);
        player.openInventory(arenaSettings.getInventory());
    }

    protected void arenaKits(Player p) {
        SGMenu arenaKits = core.getGUI().create("&6 Arena Locations", 5);
        for (Map.Entry<Object, ArrayList<ItemStack>> k : ArenaData.kits.entrySet()) {
            Material material = Material.getMaterial(core.getConfig().getString("Arena.Kits." + k.getKey() + ".Icon"));
            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            for (String str : core.getConfig().getStringList("Arena.Kits." + k.getKey() + ".Desc")) {
                lore.add(str);
            }
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    core.getConfig().get("Arena.Kits." + k.getKey() + ".Name").toString()));
            if (k.getKey().equals(core.getConfig().getString("Arena.SelectedKit"))) {
                itemMeta.addEnchant(Enchantment.MENDING, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(itemMeta);
            SGButton kit = new SGButton(new ItemBuilder(item).build()).withListener((InventoryClickEvent e) -> {
                switch (e.getClick()) {
                    case RIGHT -> {
                        SGMenu kits = core.getGUI().create(k.getKey() + " Kit Preview", 4);
                        for (ItemStack v : k.getValue()) {
                            SGButton button = new SGButton(new ItemBuilder(v).build());
                            kits.addButton(button);
                        }
                        e.getWhoClicked().openInventory(kits.getInventory());
                    }
                    case MIDDLE -> {
                        ArenaData.selectedKit = k.getKey().toString();
                        core.getConfig().set("Arena.SelectedKit", k.getKey());
                        core.saveConfig();
                        arenaKits((Player) e.getWhoClicked());
                    }
                }
            });
            arenaKits.addButton(kit);
        }
        p.openInventory(arenaKits.getInventory());
    }

    protected void arenaLocations(Player p) {
        SGMenu arenaLocations = core.getGUI().create("&6 Arena Locations", 5);
        for (Location loc : ArenaData.locations) {
            Material material = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType();
            if (material.isAir() || !material.isOccluding()) {
                material = Material.BARRIER;
            }
            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add("World : X : Y : Z");
            lore.add(loc.getWorld().getName() + " : " + loc.getBlockX() + " : " + loc.getBlockY() + " : " + loc.getBlockZ());
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            SGButton location = new SGButton(
                    new ItemBuilder(item).build())
                    .withListener((InventoryClickEvent e) -> {
                        if (e.getClick().equals(ClickType.LEFT) || e.getClick().equals(ClickType.RIGHT)) {
                            List<String> list = core.getConfig().getStringList("Arena.Locations");
                            ArenaData.locations.remove(loc);
                            list.remove(util.locationToString(loc));
                            core.getConfig().set("Arena.Locations", list);
                            core.saveConfig();
                            arenaLocations((Player) e.getWhoClicked());
                        } else if (e.getClick().equals(ClickType.MIDDLE)) {
                            Location lc = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()).getLocation();
                            e.getWhoClicked().teleport(lc);
                        }
                    });
            arenaLocations.addButton(location);
        }
        p.openInventory(arenaLocations.getInventory());
    }
}
