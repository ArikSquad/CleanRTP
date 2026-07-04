package eu.mikart.cleanrtp.references.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.types.CmdLocation;
import eu.mikart.cleanrtp.player.rtp.RTP;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpErrorRequestReason;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.WarningHandler;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.placeholder.PlaceholderAnalyzer;
import eu.mikart.cleanrtp.references.messages.placeholder.Placeholders;
import eu.mikart.cleanrtp.references.rtpinfo.PermissionGroup;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldLocation;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPermissionGroup;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;

public class RtpHelper {

    //Teleported and Sender are the same
    public static void tp(Player player,
                          World world,
                          List<String> biomes,
                          RtpType rtpType) {
        tp(player, player, world, biomes, rtpType);
    }

    //Teleported and Sender MAY be different
    public static void tp(Player player,
                          CommandSender sendi,
                          World world,
                          List<String> biomes,
                          RtpType rtpType) {
        tp(player, sendi, world, biomes, rtpType, false, false);
    }

    //
    public static void tp(Player player,
                          CommandSender sendi,
                          World world,
                          List<String> biomes,
                          RtpType rtpType,
                          boolean ignoreCooldown,
                          boolean ignoreDelay) {
        tp(player, sendi, world, biomes, rtpType, ignoreCooldown, ignoreDelay, null);
    }

    public static void tp(@NotNull Player player,
                          CommandSender sendi,
                          @Nullable World world,
                          List<String> biomes,
                          RtpType rtpType,
                          boolean ignoreCooldown,
                          boolean ignoreDelay,
                          @Nullable WorldLocation location) {
        tp(player, sendi, world, biomes, rtpType, location, new RtpPlayerInfo(!ignoreDelay, true, !ignoreCooldown));
    }

    public static void tp(@NotNull Player player,
                          CommandSender sendi,
                          @Nullable World world,
                          List<String> biomes,
                          RtpType rtpType,
                          @Nullable WorldLocation location,
                          @NotNull RtpPlayerInfo playerInfo) {
        world = getActualWorld(player, world, location);
        RTPSetupInformation setup_info = new RTPSetupInformation(
                world,
                sendi,
                player,
                true,
                biomes,
                rtpType,
                location,
                playerInfo
        );
        tp(player, sendi, setup_info);
    }

    public static void tp(@NotNull Player player,
                          CommandSender sendi,
                          @NotNull RTPSetupInformation setup_info) {
        //RTP request cancelled reason
        WorldPlayer pWorld = getPlayerWorld(setup_info);
        RtpErrorRequestReason cantReason = RtpCheckHelper.canRTP(player, sendi, pWorld, setup_info.getPlayerInfo());
        if (cantReason != null) {
            List<ComponentLike> args = new ArrayList<>(PlaceholderAnalyzer.arguments(player, pWorld));
            if (cantReason == RtpErrorRequestReason.COOLDOWN) {
                String cooldown = HelperDate.total(RtpCheckHelper.getCooldown(player, pWorld));
                args.add(Argument.string(Placeholders.COOLDOWN.key(), cooldown));
                args.add(Argument.string(Placeholders.TIME.key(), cooldown));
            }
            Component msg = Message.prefixed(Component.translatable(cantReason.getMsg().key(), args));
            RtpMessage.sms(player, msg);
            if (sendi != player)
                RtpMessage.sms(sendi, msg);
            return;
        }

        //Start teleport sequence!
        BetterRTP.getInstance().getRTP().start(pWorld);
    }

    public static World getActualWorld(Player player,
                                       World world,
                                       @Nullable WorldLocation location) {
        if (world == null)
            world = player.getWorld();
        if (location != null)
            world = location.getWorld();
        if (BetterRTP.getInstance().getRTP().overriden.containsKey(world.getName()))
            world = Bukkit.getWorld(BetterRTP.getInstance().getRTP().overriden.get(world.getName()));
        return world;
    }

    public static World getActualWorld(Player player, World world) {
        return getActualWorld(player, world, null);
    }

    @Nullable
    public static WorldLocation getRandomLocation(CommandSender sender,
                                                  World world) {
        HashMap<String, RTPWorld> locations_permissible = CmdLocation.getLocations(sender, world);
        if (!locations_permissible.isEmpty()) {
            List<String> valuesList = new ArrayList<>(locations_permissible.keySet());
            String randomIndex = valuesList.get(new Random().nextInt(valuesList.size()));
            return (WorldLocation) locations_permissible.get(randomIndex);
        }
        return null;
    }

    public static WorldPlayer getPlayerWorld(RTPSetupInformation setup_info) {
        WorldPlayer pWorld = new WorldPlayer(setup_info);

        //Random Location
        if (setup_info.getLocation() == null
                && BetterRTP.getInstance().getSettings().isLocationEnabled()
                && BetterRTP.getInstance().getSettings().isUseLocationIfAvailable()) {
            WorldLocation worldLocation = RtpHelper.getRandomLocation(setup_info.getSender(), setup_info.getWorld());
            if (worldLocation != null) {
                setup_info.setLocation(worldLocation);
                setup_info.setWorld(worldLocation.getWorld());
            }
            if (setup_info.getLocation() == null && BetterRTP.getInstance().getSettings().getGeneral().isDebug())
                WarningHandler.warn(WarningHandler.WARNING.USELOCATION_ENABLED_NO_LOCATION_AVAILABLE,
                        "This is not an error! UseLocationIfAvailable is set to `true`, but no location was found for "
                                + setup_info.getSender().getName() + "! Using world defaults! (Maybe they dont have permission?)");
        }
        //Location
        if (setup_info.getLocation() != null) {
            String setup_name = null;
            for (Map.Entry<String, RTPWorld> location_set : BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet()) {
                RTPWorld location = location_set.getValue();
                if (location == setup_info.getLocation()) {
                    setup_name = location_set.getKey();
                    break;
                }
            }
            pWorld.setup(setup_name, setup_info.getLocation(), setup_info.getLocation().getBiomes());
            //BetterRTP.getInstance().getLogger().info("Location x: " + setup_info.getLocation().getCenterX());
        }

        //Setup world (if no location pre-setup)
        if (!pWorld.isSetup()) {
            WorldPermissionGroup group = getGroup(pWorld);

            //Permission Group
            if (group != null) {
                pWorld.setup(null, group, setup_info.getBiomes());
                pWorld.config = group;
            }
            //Custom World
            else if (BetterRTP.getInstance().getRTP().getRTPcustomWorld().containsKey(setup_info.getWorld().getName())) {
                RTPWorld cWorld = BetterRTP.getInstance().getRTP().getRTPcustomWorld().get(pWorld.getWorld().getName());
                pWorld.setup(null, cWorld, setup_info.getBiomes());
            }
            //Default World
            else
                pWorld.setup(null, BetterRTP.getInstance().getRTP().getRTPdefaultWorld(), setup_info.getBiomes());
        }
        //World type
        pWorld.setWorldtype(getWorldType(pWorld.getWorld()));
        return pWorld;
    }

    public static WorldType getWorldType(World world) {
        WorldType world_type;
        RTP rtp = BetterRTP.getInstance().getRTP();
        if (rtp.world_type.containsKey(world.getName()))
            world_type = rtp.world_type.get(world.getName());
        else {
            world_type = WorldType.NORMAL;
            rtp.world_type.put(world.getName(), world_type); //Defaults this so the error message isn't spammed
            WarningHandler.warn(WarningHandler.WARNING.NO_WorldType_DECLARED, "Seems like the world `" + world.getName() + "` does not have a `WorldType` declared. " +
                    "Please add/fix this in the config.yml file! This world will be treated as an overworld! " +
                    "If this world is a nether world, configure it to NETHER (example: `- " + world.getName() + ": NETHER`", false);
        }
        return world_type;
    }

    public static WorldPermissionGroup getGroup(WorldPlayer pWorld) {
        WorldPermissionGroup group = null;
        if (pWorld.getPlayer() != null)
            for (Map.Entry<String, PermissionGroup> permissionGroup : BetterRTP.getInstance().getRTP().getPermissionGroups().entrySet()) {
                for (Map.Entry<String, WorldPermissionGroup> worldPermission : permissionGroup.getValue().getWorlds().entrySet()) {
                    if (pWorld.getWorld().equals(worldPermission.getValue().getWorld())) {
                        if (PermissionCheck.getPermissionGroup(pWorld.getPlayer(), permissionGroup.getKey())) {
                            if (group != null) {
                                if (group.getPriority() < worldPermission.getValue().getPriority())
                                    continue;
                            }
                            group = worldPermission.getValue();
                        }
                    }
                }
            }
        return group;
    }
}
