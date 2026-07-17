package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class WorldCustom extends ConfiguredRtpWorld {
    public WorldCustom(World world) {
        this.world = world;
        setupDefaults();
        BetterRTP.getInstance().getSettings().findCustomWorld(world.getName()).ifPresent(settings ->
                settings.applyTo(this, BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled()));
        validate();
    }

    public WorldCustom(World world, RTPWorld source) {
        setAllFrom(source);
        this.world = world;
    }

    private void validate() {
        if (maxRadius <= 0) {
            RtpMessage.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world.getName()
                    + "' has an invalid maximum radius; using the default.");
            maxRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }
        if (minRadius < 0 || minRadius >= maxRadius) {
            RtpMessage.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world.getName()
                    + "' has an invalid minimum radius; using the default.");
            minRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
        }
    }
}
