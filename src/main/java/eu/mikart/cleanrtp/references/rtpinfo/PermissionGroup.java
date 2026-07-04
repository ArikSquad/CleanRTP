package eu.mikart.cleanrtp.references.rtpinfo;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPermissionGroup;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class PermissionGroup {

    String groupName;
    @Getter private final HashMap<String, WorldPermissionGroup> worlds = new HashMap<>();

    public PermissionGroup(Settings.PermissionGroupDefinition definition) {
        this.groupName = definition.name();

        BetterRTP.debug("- Permission Group: " + groupName);
        for (Map<String, Settings.WorldOverrideSettings> worldList : definition.worlds()) {
            for (Map.Entry<String, Settings.WorldOverrideSettings> worldFields : worldList.entrySet()) {
                BetterRTP.debug("- -- World: " + worldFields.getKey());
                World world = Bukkit.getWorld(worldFields.getKey());
                if (world != null) {
                    WorldPermissionGroup permissionGroup = new WorldPermissionGroup(groupName, world, worldFields);
                    this.worlds.put(worldFields.getKey(), permissionGroup);
                } else
                    BetterRTP.debug("- - The Permission Group '" + groupName + "'s world '" + worldFields.getKey() + "' does not exist! Permission Group not loaded...");
            }
        }
    }

}
