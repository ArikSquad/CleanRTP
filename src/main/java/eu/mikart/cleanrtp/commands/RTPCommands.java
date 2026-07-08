package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandAfterEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandEvent;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.player.rtp.effects.RtpEffectParticles;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.util.RtpEditWorldsHelper;
import eu.mikart.cleanrtp.util.RtpHelper;
import eu.mikart.cleanrtp.util.RtpInfoHelper;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import eu.mikart.cleanrtp.references.messages.MessagesUsage;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.rtpinfo.QueueData;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldDefault;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldLocation;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import xyz.xenondevs.particle.ParticleEffect;

@Command({"betterrtp", "brtp", "rtp", "randomtp", "wild", "wildtp"})
@CommandPermission("betterrtp.use")
public final class RTPCommands {

    private static final String LABEL = "betterrtp";

    @CommandPlaceholder
    public void teleport(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("teleport", PermissionNode.USE), () -> teleport(actor.sender(), null, null));
    }

    @Subcommand("biome")
    @CommandPermission("betterrtp.biome")
    public void biome(BukkitCommandActor actor, @Named("biomes") String biome, @Optional @Named("more_biomes") String[] moreBiomes) {
        run(actor.sender(), new CommandMeta("biome", PermissionNode.BIOME), () -> {
            List<String> biomes = new ArrayList<>();
            biomes.add(biome);
            if (moreBiomes != null) biomes.addAll(Arrays.asList(moreBiomes));
            teleport(actor.sender(), null, RtpInfoHelper.getBiomes(args("biome", biomes), 1, actor.sender()));
        });
    }

    @Subcommand("biome")
    @CommandPermission("betterrtp.biome")
    public void biomeUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("biome", PermissionNode.BIOME),
                () -> MessagesUsage.BIOME.send(actor.sender(), LABEL));
    }

    @Subcommand("world")
    @CommandPermission("betterrtp.world")
    public void world(BukkitCommandActor actor, @Named("world") String worldName, @Optional @Named("biomes") String[] biomes) {
        run(actor.sender(), new CommandMeta("world", PermissionNode.WORLD), () -> {
            World world = Bukkit.getWorld(worldName);
            if (world == null) world = Bukkit.getWorld(worldName.replace("_", " "));
            if (world == null) {
                MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                return;
            }
            teleport(actor.sender(), world, RtpInfoHelper.getBiomes(args("world", worldName, biomes), 2, actor.sender()));
        });
    }

    @Subcommand("world")
    @CommandPermission("betterrtp.world")
    public void worldUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("world", PermissionNode.WORLD),
                () -> MessagesUsage.WORLD.send(actor.sender(), LABEL));
    }

    @Subcommand("player")
    @CommandPermission("betterrtp.player")
    public void player(BukkitCommandActor actor, @Named("player") String playerName, @Optional @Named("world") String worldName,
                       @Optional @Named("flags") String[] flags) {
        run(actor.sender(), new CommandMeta("player", PermissionNode.RTP_OTHER), () -> {
            Player target = Bukkit.getPlayer(playerName);
            if (target == null || !target.isOnline()) {
                MessagesCore.NOTONLINE.send(actor.sender(), playerName);
                return;
            }

            World world = target.getWorld();
            if (worldName != null) {
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                    return;
                }
            }

            RtpHelper.tp(target, actor.sender(), world, null, RtpType.FORCED, null, getFlags(flags));
        });
    }

    @Subcommand("player")
    @CommandPermission("betterrtp.player")
    public void playerUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("player", PermissionNode.RTP_OTHER),
                () -> MessagesUsage.RTP_OTHER.send(actor.sender(), LABEL));
    }

    @Subcommand("player_sudo")
    @CommandPermission("betterrtp.player")
    public void playerSudo(BukkitCommandActor actor, @Named("player") String playerName, @Optional @Named("world") String worldName) {
        run(actor.sender(), new CommandMeta("player_sudo", PermissionNode.RTP_OTHER), () -> {
            Player target = Bukkit.getPlayer(playerName);
            if (target == null || !target.isOnline()) {
                MessagesCore.NOTONLINE.send(actor.sender(), playerName);
                return;
            }

            World world = target.getWorld();
            if (worldName != null) {
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                    return;
                }
            }

            RtpHelper.tp(target, actor.sender(), world, null, RtpType.FORCED, null,
                    new RtpPlayerInfo(false, true, false, false, false));
        });
    }

    @Subcommand("player_sudo")
    @CommandPermission("betterrtp.player")
    public void playerSudoUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("player_sudo", PermissionNode.RTP_OTHER),
                () -> MessagesUsage.RTP_OTHER.send(actor.sender(), LABEL));
    }

    @Subcommand("location")
    @CommandPermission("betterrtp.location")
    public void location(BukkitCommandActor actor, @Named("location") String locationName, @Optional @Named("player") String playerName) {
        run(actor.sender(), new CommandMeta("location", PermissionNode.LOCATION), () -> {
            if (!BetterRTP.getInstance().getSettings().isLocationEnabled()) {
                MessagesUsage.LOCATION.send(actor.sender(), LABEL);
                return;
            }

            if (playerName != null && !PermissionNode.RTP_OTHER.check(actor.sender())) {
                MessagesCore.NOPERMISSION.send(actor.sender(), PermissionNode.RTP_OTHER);
                return;
            }

            Player target;
            World filterWorld = null;
            if (playerName == null) {
                if (!(actor.sender() instanceof Player player)) {
                    actor.sender().sendMessage("Console is not able to execute this command! Try '/rtp help'");
                    return;
                }
                target = player;
                filterWorld = player.getWorld();
            } else {
                target = Bukkit.getPlayer(playerName);
                if (target == null || !target.isOnline()) {
                    MessagesCore.NOTONLINE.send(actor.sender(), playerName);
                    return;
                }
            }

            for (Map.Entry<String, RTPWorld> location : LocationAccess.getLocations(actor.sender(), filterWorld).entrySet()) {
                if (location.getKey().equalsIgnoreCase(locationName)) {
                    RtpHelper.tp(target, actor.sender(), null, null, RtpType.COMMAND, false, false,
                            (WorldLocation) location.getValue());
                    return;
                }
            }
            MessagesUsage.LOCATION.send(actor.sender(), LABEL);
        });
    }

    @Subcommand("location")
    @CommandPermission("betterrtp.location")
    public void locationUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("location", PermissionNode.LOCATION),
                () -> MessagesUsage.LOCATION.send(actor.sender(), LABEL));
    }

    @Subcommand("help")
    public void help(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("help", PermissionNode.USE), () -> {
            List<ComponentLike> list = new ArrayList<>();
            list.add(MessagesHelp.PREFIX.getComponent());
            list.add(Message.translatableRaw(actor.sender(), MessagesHelp.MAIN.key(), LABEL));
            helpLine(actor.sender(), list, PermissionNode.BIOME, MessagesHelp.BIOME);
            helpLine(actor.sender(), list, PermissionNode.EDIT, MessagesHelp.EDIT);
            helpLine(actor.sender(), list, PermissionNode.USE, MessagesHelp.HELP);
            helpLine(actor.sender(), list, PermissionNode.INFO, MessagesHelp.INFO);
            if (BetterRTP.getInstance().getSettings().isLocationEnabled()) {
                helpLine(actor.sender(), list, PermissionNode.LOCATION, MessagesHelp.LOCATION);
            }
            helpLine(actor.sender(), list, PermissionNode.RTP_OTHER, MessagesHelp.PLAYER);
            helpLine(actor.sender(), list, PermissionNode.RELOAD, MessagesHelp.RELOAD);
            if (BetterRTP.getInstance().getSettings().getGeneral().isDebug()) {
                helpLine(actor.sender(), list, PermissionNode.ADMIN, MessagesHelp.TEST);
            }
            helpLine(actor.sender(), list, PermissionNode.VERSION, MessagesHelp.VERSION);
            helpLine(actor.sender(), list, PermissionNode.WORLD, MessagesHelp.WORLD);
            RtpMessage.sms(actor.sender(), list);
        });
    }

    @Subcommand("reload")
    @CommandPermission("betterrtp.reload")
    public void reload(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("reload", PermissionNode.RELOAD), () -> BetterRTP.getInstance().reload(actor.sender()));
    }

    @Subcommand("version")
    @CommandPermission("betterrtp.version")
    public void version(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("version", PermissionNode.VERSION),
                () -> RtpMessage.sms(actor.sender(), "&aVersion #&e" + BetterRTP.getInstance().getDescription().getVersion()));
    }

    @Subcommand("test")
    @CommandPermission("betterrtp.admin")
    public void test(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("test", PermissionNode.ADMIN), () -> {
            if (!BetterRTP.getInstance().getSettings().getGeneral().isDebug()) {
                MessagesCore.INVALID.send(actor.sender(), LABEL);
                return;
            }
            if (actor.sender() instanceof Player player) {
                BetterRTP.getInstance().getRTP().getTeleport().afterTeleport(player, player.getLocation(),
                        RtpHelper.getPlayerWorld(new RTPSetupInformation(player.getWorld(), player, player, false)),
                        0, player.getLocation(), RtpType.TEST);
            } else {
                actor.sender().sendMessage("Console is not able to execute this command! Try '/rtp help'");
            }
        });
    }

    @Subcommand("info")
    @CommandPermission("betterrtp.info")
    public void info(BukkitCommandActor actor, @Optional @Named("upload") String upload) {
        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO),
                () -> infoWorld(actor.sender(), LABEL, args("info", upload)));
    }

    @Subcommand("info particles")
    @CommandPermission("betterrtp.info")
    public void infoParticles(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO), () -> {
            List<String> info = new ArrayList<>();
            for (ParticleEffect effect : ParticleEffect.VALUES) {
                info.add((info.isEmpty() || info.size() % 2 == 0 ? "&7" : "&f") + effect.name() + "&r");
            }
            actor.sender().sendMessage(Message.component(info.toString(), actor.sender()));
        });
    }

    @Subcommand("info shapes")
    @CommandPermission("betterrtp.info")
    public void infoShapes(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO), () -> {
            List<String> info = new ArrayList<>();
            for (String shape : RtpEffectParticles.shapeTypes) {
                info.add((info.isEmpty() || info.size() % 2 == 0 ? "&7" : "&f") + shape + "&r");
            }
            actor.sender().sendMessage(Message.component(info.toString(), actor.sender()));
        });
    }

    @Subcommand("info potion_effects")
    @CommandPermission("betterrtp.info")
    public void infoPotionEffects(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO), () -> {
            List<String> info = new ArrayList<>();
            for (PotionEffectType effect : PotionEffectType.values()) {
                info.add((info.isEmpty() || info.size() % 2 == 0 ? "&7" : "&f") + effect.getName() + "&r");
            }
            actor.sender().sendMessage(Message.component(info.toString(), actor.sender()));
        });
    }

    @Subcommand("info world")
    @CommandPermission("betterrtp.info")
    public void infoWorld(BukkitCommandActor actor, @Named("world") String worldName, @Optional @Named("player") String playerName,
                          @Optional @Named("upload") String upload) {
        String normalizedPlayer = playerName;
        String normalizedUpload = upload;
        if ("_UPLOAD_".equalsIgnoreCase(normalizedPlayer)) {
            normalizedUpload = normalizedPlayer;
            normalizedPlayer = null;
        }
        final String targetPlayer = normalizedPlayer;
        final String uploadFlag = normalizedUpload;

        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO), () -> {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                MessagesCore.DISABLED_WORLD.send(actor.sender(), worldName);
                return;
            }
            Player player = null;
            if (targetPlayer != null) {
                player = Bukkit.getPlayer(targetPlayer);
                if (player == null) {
                    MessagesCore.NOTONLINE.send(actor.sender(), targetPlayer);
                    return;
                }
            }
            sendInfoWorld(actor.sender(), infoGetWorld(actor.sender(), world, player, null), LABEL,
                    args("info", "world", worldName, targetPlayer, uploadFlag));
        });
    }

    @Subcommand("info player")
    @CommandPermission("betterrtp.info")
    public void infoPlayer(BukkitCommandActor actor, @Named("player") String playerName, @Optional @Named("upload") String upload) {
        run(actor.sender(), new CommandMeta("info", PermissionNode.INFO), () -> {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                MessagesCore.NOTONLINE.send(actor.sender(), playerName);
                return;
            }
            sendInfoWorld(actor.sender(), infoGetWorld(actor.sender(), player.getWorld(), player, null), LABEL,
                    args("info", "player", playerName, upload));
        });
    }

    @Subcommand("queue")
    @CommandPermission("betterrtp.admin")
    public void queue(BukkitCommandActor actor, @Optional @Named("world") String worldName, @Optional @Named("upload") String upload) {
        String normalizedWorld = worldName;
        String normalizedUpload = upload;
        if ("_UPLOAD_".equalsIgnoreCase(normalizedWorld)) {
            normalizedUpload = normalizedWorld;
            normalizedWorld = null;
        }
        final String targetWorld = normalizedWorld;
        final String uploadFlag = normalizedUpload;

        run(actor.sender(), new CommandMeta("queue", PermissionNode.ADMIN), () -> {
            if (!BetterRTP.getInstance().getSettings().getGeneral().isDebug()) {
                MessagesCore.INVALID.send(actor.sender(), LABEL);
                return;
            }
            if (!(actor.sender() instanceof Player player)) {
                actor.sender().sendMessage("Console is not able to execute this command! Try '/rtp help'");
                return;
            }
            World world = targetWorld == null ? null : Bukkit.getWorld(targetWorld);
            AsyncHandler.async(() -> {
                if (world != null) {
                    sendQueueInfo(actor.sender(), queueGetWorld(player, world), LABEL, args("queue", targetWorld, uploadFlag));
                } else {
                    queueWorlds(player, LABEL, args("queue", targetWorld, uploadFlag));
                }
            });
        });
    }

    @Subcommand("edit")
    @CommandPermission("betterrtp.edit")
    public void editUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT),
                () -> MessagesUsage.EDIT_BASE.send(actor.sender(), LABEL));
    }

    @Subcommand("edit default")
    @CommandPermission("betterrtp.edit")
    public void editDefault(BukkitCommandActor actor, @Named("setting") String settingName, @Named("value") String value) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            EditCommandSetting setting = editSetting(settingName);
            if (setting == null) {
                usage(actor.sender(), EditCommandType.DEFAULT);
                return;
            }
            RtpEditWorldsHelper.editDefault(actor.sender(), setting, value);
        });
    }

    @Subcommand("edit customworld")
    @CommandPermission("betterrtp.edit")
    public void editCustomWorld(BukkitCommandActor actor, @Named("world") String worldName, @Named("setting") String settingName,
                                @Named("value") String value) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            if (Bukkit.getWorld(worldName) == null) {
                MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                return;
            }
            EditCommandSetting setting = editSetting(settingName);
            if (setting == null) {
                usage(actor.sender(), EditCommandType.CUSTOMWORLD);
                return;
            }
            RtpEditWorldsHelper.editCustomWorld(actor.sender(), setting, worldName, value);
        });
    }

    @Subcommand("edit location")
    @CommandPermission("betterrtp.edit")
    public void editLocation(BukkitCommandActor actor, @Named("location") String locationName, @Named("setting") String settingName,
                             @Named("value") String value) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            if (!BetterRTP.getInstance().getRTP().getRTPworldLocations().containsKey(locationName)) {
                usage(actor.sender(), EditCommandType.LOCATION);
                return;
            }
            EditCommandSetting setting = editSetting(settingName);
            if (setting == null) {
                usage(actor.sender(), EditCommandType.LOCATION);
                return;
            }
            RtpEditWorldsHelper.editLocation(actor.sender(), setting, locationName, value);
        });
    }

    @Subcommand("edit permission_group")
    @CommandPermission("betterrtp.edit")
    public void editPermissionGroup(BukkitCommandActor actor, @Named("group") String group, @Named("world") String worldName,
                                    @Named("setting") String settingName, @Named("value") String value) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            if (!BetterRTP.getInstance().getSettings().getPermissionGroup().isEnabled()
                    || !BetterRTP.getInstance().getRTP().getPermissionGroups().containsKey(group)) {
                usage(actor.sender(), EditCommandType.PERMISSION_GROUP);
                return;
            }
            if (Bukkit.getWorld(worldName) == null) {
                MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                return;
            }
            EditCommandSetting setting = editSetting(settingName);
            if (setting == null) {
                usage(actor.sender(), EditCommandType.PERMISSION_GROUP);
                return;
            }
            RtpEditWorldsHelper.editPermissionGroup(actor.sender(), setting, group, worldName, value);
        });
    }

    @Subcommand("edit worldtype")
    @CommandPermission("betterrtp.edit")
    public void editWorldType(BukkitCommandActor actor, @Named("world") String worldName, @Named("type") String type) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            if (Bukkit.getWorld(worldName) == null) {
                MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                return;
            }
            RtpEditWorldsHelper.editWorldtype(actor.sender(), worldName, type);
        });
    }

    @Subcommand("edit override")
    @CommandPermission("betterrtp.edit")
    public void editOverride(BukkitCommandActor actor, @Named("world") String worldName, @Named("target") String targetWorld) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT), () -> {
            if (Bukkit.getWorld(worldName) == null) {
                MessagesCore.NOTEXIST.send(actor.sender(), worldName);
                return;
            }
            RtpEditWorldsHelper.editOverride(actor.sender(), worldName, targetWorld);
        });
    }

    @Subcommand("edit blacklistedblocks add")
    @CommandPermission("betterrtp.edit")
    public void addBlacklistedBlock(BukkitCommandActor actor, @Named("block") String block) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT),
                () -> RtpEditWorldsHelper.editBlacklisted(actor.sender(), block, true));
    }

    @Subcommand("edit blacklistedblocks remove")
    @CommandPermission("betterrtp.edit")
    public void removeBlacklistedBlock(BukkitCommandActor actor, @Named("block") String block) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT),
                () -> RtpEditWorldsHelper.editBlacklisted(actor.sender(), block, false));
    }

    @Subcommand("edit blacklistedblocks")
    @CommandPermission("betterrtp.edit")
    public void blacklistedBlocksUsage(BukkitCommandActor actor) {
        run(actor.sender(), new CommandMeta("edit", PermissionNode.EDIT),
                () -> usage(actor.sender(), EditCommandType.BLACKLISTEDBLOCKS));
    }

    private void run(CommandSender sender, CommandMeta command, Runnable action) {
        if (!PermissionNode.USE.check(sender)) {
            MessagesCore.NOPERMISSION.send(sender, PermissionNode.USE);
            return;
        }
        if (!command.permission().check(sender)) {
            MessagesCore.NOPERMISSION.send(sender, command.permission());
            return;
        }

        RtpCommandEvent event = new RtpCommandEvent(sender, command);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        try {
            BetterRTP.debug(sender.getName() + " executed: /" + LABEL + " " + command.name());
            action.run();
            Bukkit.getServer().getPluginManager().callEvent(new RtpCommandAfterEvent(sender, command));
        } catch (NullPointerException e) {
            e.printStackTrace();
            RtpMessage.sms(sender, "&cERROR &7Seems like your Administrator did not update their language file!");
        }
    }

    private void teleport(CommandSender sender, World world, List<String> biomes) {
        if (sender instanceof Player player) {
            RtpHelper.tp(player, sender, world, biomes, RtpType.COMMAND);
        } else {
            RtpMessage.sms(sender, "Must be a player to use this command! Try '/" + LABEL + " help'");
        }
    }

    private RtpPlayerInfo getFlags(String[] flags) {
        boolean applyDelay = true;
        boolean applyCooldown = true;
        boolean checkCooldown = true;
        boolean takeMoney = true;
        boolean takeHunger = true;

        if (flags != null) {
            for (String arg : flags) {
                for (RtpPlayerInfo.RTP_PLAYERINFO_FLAG flag : RtpPlayerInfo.RTP_PLAYERINFO_FLAG.values()) {
                    if (flag.name().equalsIgnoreCase(arg)) {
                        switch (flag) {
                            case NODELAY -> applyDelay = false;
                            case NOCOOLDOWN -> applyCooldown = false;
                            case IGNORECOOLDOWN -> checkCooldown = false;
                            case IGNOREMONEY -> takeMoney = false;
                            case IGNOREHUNGER -> takeHunger = false;
                        }
                    }
                }
            }
        }
        return new RtpPlayerInfo(applyDelay, applyCooldown, checkCooldown, takeMoney, takeHunger);
    }

    private void helpLine(CommandSender sender, List<ComponentLike> list, PermissionNode permission, MessagesHelp message) {
        if (permission.check(sender)) {
            list.add(Message.translatableRaw(sender, message.key(), LABEL));
        }
    }

    private void usage(CommandSender sender, EditCommandType type) {
        switch (type) {
            case DEFAULT -> MessagesUsage.EDIT_DEFAULT.send(sender, LABEL);
            case CUSTOMWORLD -> MessagesUsage.EDIT_WORLD.send(sender, LABEL);
            case WorldType -> MessagesUsage.EDIT_WORLDTYPE.send(sender, LABEL);
            case OVERRIDE -> MessagesUsage.EDIT_OVERRIDE.send(sender, LABEL);
            case BLACKLISTEDBLOCKS -> MessagesUsage.EDIT_BLACKLISTEDBLLOCKS.send(sender, LABEL);
            case PERMISSION_GROUP -> MessagesUsage.EDIT_PERMISSIONGROUP.send(sender, LABEL);
            case LOCATION -> MessagesUsage.EDIT_LOCATION.send(sender, LABEL);
        }
    }

    private EditCommandSetting editSetting(String name) {
        for (EditCommandSetting setting : EditCommandSetting.values()) {
            if (setting.name().equalsIgnoreCase(name)) return setting;
        }
        return null;
    }

    private String[] args(String command, List<String> remaining) {
        String[] args = new String[remaining.size() + 1];
        args[0] = command;
        for (int i = 0; i < remaining.size(); i++) args[i + 1] = remaining.get(i);
        return args;
    }

    private String[] args(String... values) {
        return Arrays.stream(values).filter(value -> value != null && !value.isBlank()).toArray(String[]::new);
    }

    private String[] args(String first, String second, String[] remaining) {
        List<String> args = new ArrayList<>();
        args.add(first);
        args.add(second);
        if (remaining != null) args.addAll(Arrays.asList(remaining));
        return args.toArray(String[]::new);
    }

    public static void sendInfoWorld(CommandSender sender, List<String> list, String label, String[] args) {
        boolean upload = Arrays.asList(args).contains("_UPLOAD_");
        list.add(0, "&e&m-----&6 BetterRTP &8| Info &e&m-----");

        String cmd = "/" + label + " " + String.join(" ", args);
        if (!upload) {
            Message.components(list, sender).forEach(component -> sender.sendMessage(component.asComponent()));
            if (sender instanceof Player player) {
                player.sendMessage(Message.component("&7- &7Click to upload command log to &flogs.ronanplugins.com", player)
                        .clickEvent(ClickEvent.suggestCommand(cmd + " _UPLOAD_"))
                        .hoverEvent(HoverEvent.showText(Message.component("&6Suggested command&f: &7" + cmd + " _UPLOAD_", player))));
            } else {
                sender.sendMessage(Component.text("Execute `" + cmd + " _UPLOAD_` to upload command log to https://logs.ronanplugins.com"));
            }
        } else {
            list.add(0, "Command: " + cmd);
            list.replaceAll(RTPCommands::stripLegacyColor);
        }
    }

    private static String stripLegacyColor(String value) {
        return value.replaceAll("&[0-9A-Fa-fK-Ok-oRr]", "");
    }

    private void infoWorld(CommandSender sender, String label, String[] args) {
        List<String> info = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            info.addAll(infoGetWorld(sender, world, null, null));
        }
        sendInfoWorld(sender, info, label, args);
    }

    public static List<String> infoGetWorld(CommandSender sender, World world, Player player, WorldPlayer rtpWorld) {
        List<String> info = new ArrayList<>();
        BetterRTP plugin = BetterRTP.getInstance();
        String yes = "&aTrue", no = "&bFalse";
        info.add("&bRTP info for &7" + world.getName() + (player != null ? " &d(personalized)" : ""));
        info.add("&7- &eViewing as: &b" + (player != null ? player.getName() : "ADMIN"));
        info.add("&7- &6Allowed: " + (player != null ? PermissionCheck.getAWorld(player, world.getName()) ? yes : no : "&cN/A"));
        if (plugin.getRTP().getDisabledWorlds().contains(world.getName()) && !plugin.getRTP().overriden.containsKey(world.getName())) {
            info.add("&7- &eDisabled: " + yes);
        } else {
            info.add("&7- &eDisabled: " + no);
            if (plugin.getRTP().overriden.containsKey(world.getName())) {
                world = Bukkit.getWorld(plugin.getRTP().overriden.get(world.getName()));
                info.add("&7- &6Overriden: " + yes + " &7- target `" + world.getName() + "`");
            } else {
                info.add("&7- &6Overriden&7: " + no);
            }
            if (rtpWorld == null) {
                rtpWorld = RtpHelper.getPlayerWorld(new RTPSetupInformation(world, player != null ? player : sender, player, player != null));
            }
            WorldDefault worldDefault = BetterRTP.getInstance().getRTP().getRTPdefaultWorld();
            info.add("&7- &eSetup Type&7: " + rtpWorld.setup_type.name() + getInfo(rtpWorld, worldDefault, "setup"));
            info.add("&7- &6Use World Border&7: " + (rtpWorld.getUseWorldborder() ? yes : no));
            info.add("&7- &eWorld Type&7: &f" + rtpWorld.getWorldtype().name());
            info.add("&7- &6Center X&7: &f" + rtpWorld.getCenterX() + getInfo(rtpWorld, worldDefault, "centerx"));
            info.add("&7- &eCenter Z&7: &f" + rtpWorld.getCenterZ() + getInfo(rtpWorld, worldDefault, "centerz"));
            info.add("&7- &6Max Radius&7: &f" + rtpWorld.getMaxRadius() + getInfo(rtpWorld, worldDefault, "maxrad"));
            info.add("&7- &eMin Radius&7: &f" + rtpWorld.getMinRadius() + getInfo(rtpWorld, worldDefault, "minrad"));
            info.add("&7- &6Min Y&7: &f" + rtpWorld.getMinY());
            info.add("&7- &eMax Y&7: &f" + rtpWorld.getMaxY());
            info.add("&7- &6Price&7: &f" + rtpWorld.getPrice() + getInfo(rtpWorld, worldDefault, "price"));
            info.add("&7- &eCooldown&7: &f" + rtpWorld.getCooldown() + getInfo(rtpWorld, worldDefault, "cooldown"));
            info.add("&7- &6Biomes&7: &f" + rtpWorld.getBiomes());
            info.add("&7- &eShape&7: &f" + rtpWorld.getShape() + getInfo(rtpWorld, worldDefault, "shape"));
            info.add("&7- &6Permission Group&7: " + (rtpWorld.getConfig() != null ? "&a" + rtpWorld.getConfig().getGroupName() : "&cN/A"));
            info.add("&7- &eQueue Available&7: " + (QueueHandler.isEnabled() ? QueueHandler.getApplicableAsync(rtpWorld).size() : "&cDisabled"));
        }
        return info;
    }

    private static String getInfo(WorldPlayer worldPlayer, WorldDefault worldDefault, String type) {
        return switch (type) {
            case "centerx" -> worldPlayer.getUseWorldborder() || worldPlayer.getCenterX() == worldDefault.getCenterX()
                    ? worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)" : "";
            case "centerz" -> worldPlayer.getUseWorldborder() || worldPlayer.getCenterZ() == worldDefault.getCenterZ()
                    ? worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)" : "";
            case "maxrad" -> worldPlayer.getUseWorldborder() || worldPlayer.getMaxRadius() == worldDefault.getMaxRadius()
                    ? worldPlayer.getUseWorldborder()
                    ? worldPlayer.getMaxRadius() >= worldPlayer.getWorld().getWorldBorder().getSize() ? " &8(worldborder)" : " &8(custom)"
                    : " &8(default)" : "";
            case "minrad" -> worldPlayer.getMinRadius() == worldDefault.getMinRadius() ? " &8(default)" : "";
            case "price" -> worldPlayer.getPrice() == worldDefault.getPrice() ? " &8(default)" : "";
            case "shape" -> worldPlayer.getShape() == worldDefault.getShape() ? " &8(default)" : "";
            case "setup" -> worldPlayer.setup_type == eu.mikart.cleanrtp.commands.RtpSetupType.LOCATION
                    ? " &7(" + worldPlayer.setup_name + ")" : "";
            case "cooldown" -> worldPlayer.getPlayer() != null
                    ? PermissionNode.BYPASS_COOLDOWN.check(worldPlayer.getPlayer()) ? " &8(bypassing)" : "" : " &cN/A";
            default -> "";
        };
    }

    private void sendQueueInfo(CommandSender sender, List<String> list, String label, String[] args) {
        boolean upload = Arrays.asList(args).contains("_UPLOAD_");
        list.add(0, "&e&m-----&6 BetterRTP &8| Queue &e&m-----");
        String cmd = "/" + label + " " + String.join(" ", args);
        if (!upload) {
            Message.components(list, sender).forEach(component -> sender.sendMessage(component.asComponent()));
            if (sender instanceof Player player) {
                player.sendMessage(Message.component("&7- &7Click to upload command log to &flogs.ronanplugins.com", player)
                        .clickEvent(ClickEvent.suggestCommand(cmd + " _UPLOAD_"))
                        .hoverEvent(HoverEvent.showText(Message.component("&6Suggested command&f: &7" + cmd + " _UPLOAD_", player))));
            } else {
                sender.sendMessage(Component.text("Execute `" + cmd + " _UPLOAD_` to upload command log to https://logs.ronanplugins.com"));
            }
        } else {
            list.add(0, "Command: " + cmd);
            list.replaceAll(RTPCommands::stripLegacyColor);
        }
    }

    private void queueWorlds(Player player, String label, String[] args) {
        List<String> info = new ArrayList<>();
        int locations = 0;
        for (World world : Bukkit.getWorlds()) {
            List<String> list = queueGetWorld(player, world);
            info.addAll(list);
            locations += list.size();
        }
        info.add("&eTotal of &a%amount% &egenerated locations".replace("%amount%", String.valueOf(locations)));
        sendQueueInfo(player, info, label, args);
    }

    private static List<String> queueGetWorld(Player player, World world) {
        List<String> info = new ArrayList<>();
        info.add("&eWorld: &6" + world.getName());
        RTPSetupInformation setupInfo = new RTPSetupInformation(RtpHelper.getActualWorld(player, world), player, player, true);
        WorldPlayer playerWorld = RtpHelper.getPlayerWorld(setupInfo);
        for (QueueData queue : QueueHandler.getApplicableAsync(playerWorld)) {
            Location loc = queue.getLocation();
            info.add("&8- &7x= &b" + loc.getBlockX() + ", &7z= &b" + loc.getBlockZ());
        }
        return info;
    }
}
