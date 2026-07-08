package eu.mikart.cleanrtp.versions;

import eu.mikart.cleanrtp.BetterRTP;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;

public class AsyncHandler {

    public static void async(Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(BetterRTP.getInstance(), _ -> runnable.run());
    }

    public static void sync(Runnable runnable) {
        Bukkit.getGlobalRegionScheduler().run(BetterRTP.getInstance(), _ -> runnable.run());
    }

    public static void syncAtEntity(Entity entity, Runnable runnable) {
        entity.getScheduler().run(BetterRTP.getInstance(), _ -> runnable.run(), null);
    }

    public static ScheduledTask asyncLater(Runnable runnable, long ticks) {
        return Bukkit.getAsyncScheduler().runDelayed(BetterRTP.getInstance(), _ -> runnable.run(), ticks * 50L, TimeUnit.MILLISECONDS);
    }

    public static ScheduledTask syncLater(Runnable runnable, long ticks) {
        return Bukkit.getGlobalRegionScheduler().runDelayed(BetterRTP.getInstance(), _ -> runnable.run(), ticks);
    }
}
