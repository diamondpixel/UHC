package xtr.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import xtr.uhc.Core;
import xtr.uhc.Manager.UHC;
import xtr.uhc.Manager.UHCPlayer;

public class GenericListeners implements Listener {

    private Core core = Core.instance;
    private UHC uhc = core.getUHC();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UHCPlayer player = new UHCPlayer(event.getPlayer().getUniqueId());
        uhc.getPlayers().put(event.getPlayer().getUniqueId(), player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        UHCPlayer player = new UHCPlayer(event.getPlayer().getUniqueId());
        uhc.getPlayers().remove(player);
    }

    @EventHandler
    private void onEntityPortalEnter(EntityPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            e.setCancelled(true);
            e.getPlayer().teleport(uhc.getUHCCenter());
        }
    }
}
