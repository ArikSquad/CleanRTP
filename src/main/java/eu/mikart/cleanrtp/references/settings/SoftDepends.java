package eu.mikart.cleanrtp.references.settings;

import lombok.Getter;
import lombok.Setter;
import eu.mikart.cleanrtp.references.depends.regionplugins.RegionPlugins;
import eu.mikart.cleanrtp.BetterRTP;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class SoftDepends {

    public void load() {
        for (RegionPlugins plugin : RegionPlugins.values())
            registerPlugin(plugin);
    }

    public void registerPlugin(RegionPlugins pl) {
        var respect = BetterRTP.getInstance().getSettings().getGeneral().getRespect();
        pl.getPlugin().setRespecting(switch (pl.getSettingName()) {
            case "Lands" -> respect.isLands();
            case "HuskClaims" -> respect.isHuskClaims();
            case "HuskTowns" -> respect.isHuskTowns();
            default -> false;
        });
        if (pl.getPlugin().isRespecting())
            pl.getPlugin().setEnabled(Bukkit.getPluginManager().isPluginEnabled(pl.getPluginYmlName()));
        if (pl.getPlugin().isRespecting())
            debug("Respecting `" + pl.getSettingName() + "` was " + (pl.getPlugin().enabled ? "SUCCESSFULLY" : "NOT") + " registered");
    }



    @Setter
    @Getter
    static public class RegionPlugin {
        private boolean respecting;
        private boolean enabled;
    }

    private void debug(String str) {
        if (BetterRTP.getInstance().getSettings().getGeneral().isDebug())
            BetterRTP.getInstance().getLogger().log(Level.INFO, str);
    }
}
