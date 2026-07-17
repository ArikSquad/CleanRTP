package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public final class WorldLocation extends ConfiguredRtpWorld {
    private final String name;

    public WorldLocation(String name) {
        this.name = name;
        setupDefaults();
        Settings.LocationEntry entry = BetterRTP.getInstance().getSettings().getLocations().entries.get(name);
        if (entry == null || entry.world == null || (world = Bukkit.getWorld(entry.world)) == null) {
            BetterRTP.getInstance().getLogger().warning("Location `" + name + "` has no valid world");
            return;
        }

        useWorldBorder = entry.useWorldBorder;
        centerX = entry.centerX;
        centerZ = entry.centerZ;
        maxRadius = entry.maxRadius;
        minRadius = entry.minRadius;
        biomes = entry.biomes;
        if (BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled() && entry.price > 0) {
            price = entry.price;
        }
        try {
            shape = RtpShape.valueOf(entry.shape.toUpperCase());
        } catch (IllegalArgumentException exception) {
            BetterRTP.getInstance().getLogger().warning("Location `" + name + "` has invalid shape `" + entry.shape + "`");
        }
        minY = entry.minY;
        maxY = entry.maxY;
        cooldown = entry.cooldown;
        validateRadius();
    }

    private void validateRadius() {
        RTPWorld defaults = BetterRTP.getInstance().getRTP().getRTPdefaultWorld();
        if (maxRadius <= 0) maxRadius = defaults.getMaxRadius();
        if (minRadius < 0 || minRadius >= maxRadius) {
            minRadius = defaults.getMinRadius();
            if (minRadius >= maxRadius) maxRadius = defaults.getMaxRadius();
        }
    }

    public boolean isValid() {
        return world != null;
    }

    @Nullable
    @Override
    public String getID() {
        return name;
    }
}
