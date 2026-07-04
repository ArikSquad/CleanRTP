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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
