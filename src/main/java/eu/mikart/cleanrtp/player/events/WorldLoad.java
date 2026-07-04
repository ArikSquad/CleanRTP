package eu.mikart.cleanrtp.player.events;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoad {

    WrappedTask loader;

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
