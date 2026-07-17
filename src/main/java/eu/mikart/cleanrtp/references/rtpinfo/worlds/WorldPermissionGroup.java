package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Map;

@Getter
public final class WorldPermissionGroup extends ConfiguredRtpWorld {
    private int priority;
    private final String groupName;

    public WorldPermissionGroup(String group, World world, Map.Entry<String, Settings.WorldOverrideSettings> fields) {
        groupName = group;
        this.world = world;
        setupDefaults();
        Settings.WorldOverrideSettings settings = fields.getValue();
        priority = settings.getPriority() == null ? 0 : settings.getPriority();
        settings.applyTo(this, BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled());
        validate();
    }

    private void validate() {
        if (maxRadius <= 0) {
            RtpMessage.sms(Bukkit.getConsoleSender(), "WARNING! Group '" + groupName
                    + "' has an invalid maximum radius; using the default.");
            maxRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }
        if (minRadius < 0 || minRadius >= maxRadius) {
            RtpMessage.sms(Bukkit.getConsoleSender(), "WARNING! Group '" + groupName
                    + "' has an invalid minimum radius; using the default.");
            minRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
            if (minRadius >= maxRadius) maxRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }
    }
}
