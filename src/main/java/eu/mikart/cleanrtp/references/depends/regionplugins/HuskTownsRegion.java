package eu.mikart.cleanrtp.references.depends.regionplugins;

import net.william278.husktowns.api.BukkitHuskTownsAPI;
import org.bukkit.Location;

public class HuskTownsRegion implements RegionPluginCheck {

    @Override
    public boolean check(Location loc) {
        boolean result = true;
        if (RegionPlugins.HUSK_TOWNS.isEnabled()) {
            try {
                result = !BukkitHuskTownsAPI.getInstance().getClaimAt(loc).isPresent();
            } catch (RuntimeException exception) {
                eu.mikart.cleanrtp.BetterRTP.getInstance().getLogger().log(java.util.logging.Level.WARNING,
                        "HuskTowns region check failed", exception);
            }
        }
        return result;
    }
}
