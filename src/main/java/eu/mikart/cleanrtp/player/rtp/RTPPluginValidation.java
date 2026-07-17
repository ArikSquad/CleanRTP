package eu.mikart.cleanrtp.player.rtp;

import eu.mikart.cleanrtp.references.depends.regionplugins.RegionPlugins;
import org.bukkit.Location;

public final class RTPPluginValidation {
    private RTPPluginValidation() {
    }

    /**
     * @param loc Location to check
     * @return True if valid location
     */
    public static boolean checkLocation(Location loc) {
        for (RegionPlugins validators : RegionPlugins.values())
            if (!validators.getValidator().check(loc))
                return false;
        return true;
    }
}
