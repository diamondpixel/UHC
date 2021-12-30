package xtr.uhc.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Utilities {


    public final List helmets = Arrays.asList(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.CHAINMAIL_HELMET, Material.NETHERITE_HELMET);
    public final List chestplates = Arrays.asList(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
    public final List leggings = Arrays.asList(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.NETHERITE_LEGGINGS);
    public final List boots = Arrays.asList(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.CHAINMAIL_BOOTS, Material.NETHERITE_BOOTS);

    public List<Block> getSphere(@NotNull Location centerBlock, int radius, boolean hollow) {
        List<Block> circleBlocks = new ArrayList<>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l.getBlock());
                    }
                }
            }
        }
        return circleBlocks;
    }

    public Location getLocationString(final String s) {
        if (s == null || s.trim() == "") {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            final World w = Bukkit.getServer().getWorld(parts[0]);
            final int x = Integer.parseInt(parts[1]);
            final int y = Integer.parseInt(parts[2]);
            final int z = Integer.parseInt(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }

    public String locationToString(final Location loc) {
        return (loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ());
    }

    public boolean blockWithin(World world, Block block ,Location max , Location min){
      double  maxX = Math.max(max.getX(), min.getX());
      double  maxY = Math.max(max.getY(), min.getY());
      double  maxZ = Math.max(max.getZ(), min.getZ());
      double  minX = Math.min(max.getX(), min.getX());
      double  minY = Math.min(max.getY(), min.getY());
      double  minZ = Math.min(max.getZ(), min.getZ());
            return (block.getWorld().equals(world)
                    && minX <= block.getX()
                    && minY <= block.getY()
                    && minZ <= block.getZ()
                    && maxX >= block.getX()
                    && maxY >= block.getY()
                    && maxZ >= block.getZ());
         }
}
