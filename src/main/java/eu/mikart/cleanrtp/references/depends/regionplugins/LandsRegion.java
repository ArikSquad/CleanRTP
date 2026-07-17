package eu.mikart.cleanrtp.references.depends.regionplugins;

import org.bukkit.Location;

import eu.mikart.cleanrtp.BetterRTP;
import me.angeschossen.lands.api.LandsIntegration;

public class LandsRegion implements RegionPluginCheck {

    // Implemented (2.14.3)
    // Tested (3.6.2)
    // Lands (v6.28.13)
    // https://www.spigotmc.org/resources/lands.53313/
    public boolean check(Location loc) {
        boolean result = true;
        if (RegionPlugins.LANDS.isEnabled())
            try {
                result = LandsIntegration.of(BetterRTP.getInstance()).getArea(loc) == null;
            } catch (RuntimeException exception) {
                BetterRTP.getInstance().getLogger().log(java.util.logging.Level.WARNING,
                        "Lands region check failed", exception);
            }
        return result;
    }
}
