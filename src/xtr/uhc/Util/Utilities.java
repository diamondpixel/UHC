package xtr.uhc.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Utilities {

    public int findGroundLevel(int x, int z, @NotNull World world) {
        /**Variables*/
        HashMap<Integer, Float> locations = new HashMap<>();
        List<Location> suitable_loc = new ArrayList<>();
        List<Material> air = new ArrayList<>(Arrays.asList(new Material[]{Material.AIR, Material.VOID_AIR}));
        /** Determine suitable blocks... */
        for (int max = world.getHighestBlockYAt(x, z); 50 < max; max--) {
            Material block_1 = world.getBlockAt(x, max + 1, z).getType();
            Material block_2 = world.getBlockAt(x, max + 2, z).getType();
            if (!air.contains(world.getBlockAt(x, max, z).getType()) && (air.contains(block_1) || block_1.isTransparent())
                    && (air.contains(block_2) || block_2.isTransparent())) {
                Location loc = new Location(world, x, max, z);
                suitable_loc.add(loc);
            }
        }
        /** Retrieve Air Blocks and divide Air with Total Blocks to get a air percentage
         * */
        for (Location loc : suitable_loc) {
            float i = 0;
            float j = 0;
            for (Block block : getSphere(loc, 2, false)) {
                i++;
                if (air.contains(block.getType())) {
                    j++;
                }
                if (((j / i) * 100) >= 33) {
                    locations.put((int) loc.getY(), (j / i) * 100);
                }
            }
        }

        int bestY = 320;
        int lowAir = 100;

        /**Determine best height in combination with air %*/
        for (Map.Entry map : locations.entrySet()) {
            int value = (int) Math.floor((Float) map.getValue());
            int key = (int) map.getKey();
            if (((key < bestY) && (value < lowAir))) {
                bestY = key;
                lowAir = value;
            }
        }
        return bestY;
    }

    /**
     * public List<Block> getSurround(@NotNull Location center, int radius)
     * {
     * List<Block> blocks = new ArrayList<>();
     * <p>
     * for(double x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
     * for (double z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
     * Location loc = new Location(center.getWorld(), x, center.getBlockY(), z);
     * blocks.add(loc.getBlock());
     * }
     * }
     * return blocks;
     * }
     */

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
}
