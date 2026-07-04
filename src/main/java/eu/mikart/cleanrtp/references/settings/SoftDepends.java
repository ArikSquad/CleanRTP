package eu.mikart.cleanrtp.references.settings;

import lombok.Getter;
import lombok.Setter;
import eu.mikart.cleanrtp.references.depends.regionplugins.RegionPlugins;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.file.FileOther;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class SoftDepends {

    void load() {
        for (RegionPlugins plugin : RegionPlugins.values())
            registerPlugin(plugin);
    }

    public void registerPlugin(RegionPlugins pl) {
        FileOther.Filetype config = BetterRTP.getInstance().getFiles().getType(FileOther.Filetype.CONFIG);
        String pre = "Settings.Respect.";
        pl.getPlugin().setRespecting(config.getBoolean(pre + pl.getSettingName()));
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
        if (BetterRTP.getInstance().getSettings().isDebug())
            BetterRTP.getInstance().getLogger().log(Level.INFO, str);
    }
}
