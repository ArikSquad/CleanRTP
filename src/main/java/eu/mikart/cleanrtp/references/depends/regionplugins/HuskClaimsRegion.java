package eu.mikart.cleanrtp.references.depends.regionplugins;

import net.william278.huskclaims.BukkitHuskClaims;
import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import org.bukkit.Location;

public class HuskClaimsRegion implements RegionPluginCheck {

    @Override
    public boolean check(Location loc) {
        boolean result = true;
        if (RegionPlugins.HUSK_CLAIMS.isEnabled()) {
            try {
                result = !BukkitHuskClaimsAPI.getInstance().getClaimAt(BukkitHuskClaims.Adapter.adapt(loc)).isPresent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
