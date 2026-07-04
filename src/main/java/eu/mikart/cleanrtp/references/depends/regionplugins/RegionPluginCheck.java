package eu.mikart.cleanrtp.references.depends.regionplugins;

import org.bukkit.Location;

public interface RegionPluginCheck {

    /**
     * @param loc Location to check
     * @return True if valid teleport location, false if the location is claimed and invalid
     */
    boolean check(Location loc);

}
