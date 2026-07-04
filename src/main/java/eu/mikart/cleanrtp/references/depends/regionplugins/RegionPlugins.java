package eu.mikart.cleanrtp.references.depends.regionplugins;

import lombok.Getter;
import eu.mikart.cleanrtp.references.settings.SoftDepends;

@Getter
public enum RegionPlugins {
    HUSK_CLAIMS("HuskClaims", new HuskClaimsRegion()),
    HUSK_TOWNS("HuskTowns", new HuskTownsRegion()),
    LANDS("Lands", new LandsRegion());

    private final SoftDepends.RegionPlugin plugin = new SoftDepends.RegionPlugin();
    private final String settingName;
    private final String pluginYmlName;
    private final RegionPluginCheck validator;

    RegionPlugins(String name, RegionPluginCheck validator) {
        this(name, name, validator);
    }

    RegionPlugins(String settingName, String pluginYmlName, RegionPluginCheck validator) {
        this.settingName = settingName;
        this.pluginYmlName = pluginYmlName;
        this.validator = validator;
    }

    public boolean isEnabled() {
        return plugin.isEnabled();
    }
}
