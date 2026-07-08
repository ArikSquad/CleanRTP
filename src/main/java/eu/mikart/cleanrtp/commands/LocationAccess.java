package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public final class LocationAccess {

    public static HashMap<String, RTPWorld> getLocations(CommandSender sender, @Nullable World world) {
        HashMap<String, RTPWorld> locations = new HashMap<>();
        boolean needPermission = BetterRTP.getInstance().getSettings().isLocationNeedPermission();
        boolean needSameWorld = BetterRTP.getInstance().getSettings().isUseLocationsInSameWorld();
        if (needSameWorld) {
            needSameWorld = !PermissionNode.BYPASS_LOCATION.check(sender);
        }

        for (Map.Entry<String, RTPWorld> location : BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet()) {
            boolean add = !needPermission || PermissionCheck.getLocation(sender, location.getKey());
            if (add && needSameWorld) {
                add = world == null || location.getValue().getWorld().equals(world);
            }
            if (add) {
                locations.put(location.getKey(), location.getValue());
            }
        }
        return locations;
    }
}
