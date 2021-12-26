package xtr.uhc;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import xtr.uhc.Util.Utilities;

public class TeleportationHandler implements Listener {

    private final static Core core = Core.instance;
    private final static Utilities util = core.util;

    @EventHandler
    private void onEntityPortalEnter(EntityPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            e.setCancelled(true);
            Location loc = new Location(e.getPlayer().getWorld(), 0, util.findGroundLevel(0, 0, e.getPlayer().getWorld()), 0);
            e.getPlayer().teleport(loc);
        }
    }
}
