package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoad {

    ScheduledTask loader;

    void load(WorldLoadEvent e) {
        //BetterRTP.getInstance().getLogger().info("NEW WORLD!");
        if (loader != null)
            loader.cancel();
        loader = AsyncHandler.syncLater(() -> {
            BetterRTP.debug("New world `" + e.getWorld().getName() + "` detected! Reloaded Databases!");
            BetterRTP.getInstance().getDatabaseHandler().load();
        }, 20L * 5);
    }
}
