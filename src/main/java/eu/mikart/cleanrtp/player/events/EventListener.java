package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.events.custom.RtpTeleportPostEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.PluginManager;

public class EventListener implements Listener {
    private final WorldLoad worldLoad = new WorldLoad();

    public void registerEvents(BetterRTP pl) {
        PluginManager pm = pl.getServer().getPluginManager();
        pm.registerEvents(this, pl);
    }

    public void load() {
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        Leave.event(e);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Join.event(e);
    }

    @EventHandler
    private void teleport(PlayerTeleportEvent e) {
        Teleport.tpEvent(e);
    }

    @EventHandler
    private void rtpPost(RtpTeleportPostEvent e) {
        Custom.postRTP(e);
    }

    @EventHandler
    private void worldLoad(WorldLoadEvent e) {
        worldLoad.load(e);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        Damage.onEntityDamage(e);
    }
}