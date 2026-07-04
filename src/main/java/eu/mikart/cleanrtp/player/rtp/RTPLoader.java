package eu.mikart.cleanrtp.player.rtp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.file.FileOther;
import eu.mikart.cleanrtp.references.rtpinfo.PermissionGroup;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldCustom;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldDefault;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldLocation;

public class RTPLoader {

    static void loadWorlds(@NotNull WorldDefault defaultWorld, @NotNull HashMap<String, RTPWorld> customWorlds) {
        defaultWorld.load();
        customWorlds.clear();
        BetterRTP.debug("Loading Custom Worlds...");
        try {
            FileOther.Filetype config = FileOther.Filetype.CONFIG;
            List<Map<?, ?>> map = config.getMapList("CustomWorlds");
            for (Map<?, ?> m : map)
                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    String world = entry.getKey().toString();
                    AtomicBoolean exists = new AtomicBoolean(false);
                    Bukkit.getWorlds().forEach(w -> {
                        if (w.getName().equals(world))
                            exists.set(true);
                    });
                    if (exists.get()) {
                        BetterRTP.debug("Custom World '" + world + "' registered:");
                        customWorlds.put(world, new WorldCustom(Bukkit.getWorld(world)));
                    } else
                        BetterRTP.debug("[WARN] - Custom World '" + world + "' was not registered because world does NOT exist");
                }
        } catch (Exception e) {
            //No Custom Worlds
        }
    }

    static void loadOverrides(@NotNull HashMap<String, String> overriden) {
        BetterRTP.debug("Loading Overrides...");
        overriden.clear();
        try {
            FileOther.Filetype config = FileOther.Filetype.CONFIG;
            List<Map<?, ?>> override_map = config.getMapList("Overrides");
            for (Map<?, ?> m : override_map)
                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    overriden.put(entry.getKey().toString(), entry.getValue().toString());
                    if (getPl().getSettings().isDebug())
                        getPl().getLogger().info("- Override '" + entry.getKey() + "' -> '" + entry.getValue() + "' added");
                    if (Bukkit.getWorld(entry.getValue().toString()) == null)
                        getPl().getLogger().warning("The world `" + entry.getValue() + "` doesn't seem to exist! Please update `" + entry.getKey() + "'s` override! Maybe there are capital letters?");
                }
        } catch (Exception e) {
            //No Overrides
        }
    }

    static void loadWorldTypes(@NotNull HashMap<String, WorldType> world_type) {
        BetterRTP.debug("Loading World Types...");
        world_type.clear();
        try {
            FileOther.Filetype config = FileOther.Filetype.CONFIG;
            //for (World world : Bukkit.getWorlds())
            //    world_type.put(world.getName(), WorldType.NORMAL);
            List<Map<?, ?>> world_map = config.getMapList("WorldType");
            for (Map<?, ?> m : world_map)
                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    //if (world_type.containsKey(entry.getKey())) {
                        try {
                            String world = entry.getKey().toString();
                            WorldType type = WorldType.valueOf(entry.getValue().toString().toUpperCase());
                            world_type.put(world, type);
                            BetterRTP.debug("- World Type for '" + world + "' set to '" + type + "'");
                        } catch(IllegalArgumentException e) {
                            StringBuilder valids = new StringBuilder();
                            for (WorldType type : WorldType.values())
                                valids.append(type.name()).append(", ");
                            valids.replace(valids.length() - 2, valids.length(), "");
                            getPl().getLogger().severe("World Type for '" + entry.getKey() + "' is INVALID '" + entry.getValue() +
                                    "'. Valid ID's are: " + valids);
                            //Wrong rtp world type
                        }
                    //}/* else {
                    //    if (getPl().getSettings().debug)
                    //        getPl().getLogger().info("- World Type failed for '" + entry.getKey() + "' is it loaded?");
                    //}*/
                }
        } catch (Exception e) {
            e.printStackTrace();
            //No World Types
        }
    }

    static void loadLocations(@NotNull HashMap<String, RTPWorld> worlds) {
        worlds.clear();
        FileOther.Filetype config = FileOther.Filetype.LOCATIONS;
        if (!BetterRTP.getInstance().getSettings().isLocationEnabled())
            return;
        BetterRTP.debug("Loading Locations...");
        List<Map<?, ?>> map = config.getMapList("Locations");
        for (Map<?, ?> m : map)
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                WorldLocation location = new WorldLocation(entry.getKey().toString());
                if (location.isValid()) {
                    worlds.put(entry.getKey().toString(), location);
                    BetterRTP.debug("- Location '" + entry.getKey() + "' registered");
                }
            }
    }

    static void loadPermissionGroups(@NotNull HashMap<String, PermissionGroup> permissionGroup) {
        permissionGroup.clear();
        FileOther.Filetype config = FileOther.Filetype.CONFIG;
        if (!getPl().getSettings().isPermissionGroupEnabled())
            return;
        BetterRTP.debug("Loading Permission Groups...");
        try {
            List<Map<?, ?>> map = config.getMapList("PermissionGroup.Groups");
            for (Map<?, ?> m : map)
                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    String group = entry.getKey().toString();
                    permissionGroup.put(group, new PermissionGroup(entry));
                }
        } catch (Exception e) {
            //No Permission Groups
        }
    }

    private static BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
