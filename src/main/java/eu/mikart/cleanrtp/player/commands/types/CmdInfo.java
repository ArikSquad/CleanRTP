package eu.mikart.cleanrtp.player.commands.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.player.commands.RtpSetupType;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.effects.RtpEffectParticles;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldDefault;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import xyz.xenondevs.particle.ParticleEffect;

public class CmdInfo implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "info";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase(CmdInfoSub.PARTICLES.name()))
                infoParticles(sender);
            else if (args[1].equalsIgnoreCase(CmdInfoSub.SHAPES.name()))
                infoShapes(sender);
            else if (args[1].equalsIgnoreCase(CmdInfoSub.POTION_EFFECTS.name()))
                infoEffects(sender);
            else if (args[1].equalsIgnoreCase(CmdInfoSub.WORLD.name())) {
                World world = null;
                Player player = null;
                if (args.length > 2) {
                    world = Bukkit.getWorld(args[2]);
                    if (world == null) {
                        MessagesCore.DISABLED_WORLD.send(sender, args[2]);
                        return;
                    }
                } else {
                    MessagesCore.DISABLED_WORLD.send(sender, "NULL");
                    return;
                }
                if (args.length > 3) {
                    player = Bukkit.getPlayer(args[3]);
                    if (player == null) {
                        MessagesCore.NOTONLINE.send(sender, args[2]);
                        return;
                    }
                }
                sendInfoWorld(sender, infoGetWorld(sender, world, player, null), label, args);
            } else if (args[1].equalsIgnoreCase(CmdInfoSub.PLAYER.name())) {
                World world = null;
                Player player = null;
                if (args.length > 2) {
                    player = Bukkit.getPlayer(args[2]);
                    if (player != null)
                        world = player.getWorld();
                }
                if (player == null) {
                    MessagesCore.NOTONLINE.send(sender, args.length > 2 ? args[2] : "NULL");
                    return;
                }
                if (world == null)
                    world = player.getWorld();
                sendInfoWorld(sender, infoGetWorld(sender, world, player, null), label, args);
            }
        } else
            infoWorld(sender, label, args);
    }

    @Override
    public net.kyori.adventure.text.ComponentLike getHelp(org.bukkit.command.CommandSender sender, String label) {
        return eu.mikart.cleanrtp.references.messages.Message.translatableRaw(sender, MessagesHelp.INFO.key(), label);
    }

    enum CmdInfoSub { //Sub commands, future expansions
        PARTICLES, SHAPES, POTION_EFFECTS, WORLD, PLAYER
    }

    //Particles
    private void infoParticles(CommandSender sendi) {
        List<String> info = new ArrayList<>();
        // BetterRTP pl = BetterRTP.getInstance();

        for (ParticleEffect eff : ParticleEffect.VALUES) {
            if (info.isEmpty() || info.size() % 2 == 0) {
                info.add("&7" + eff.name() + "&r");
            } else
                info.add("&f" + eff.name() + "&r");
        }

        sendi.sendMessage(Message.component(info.toString(), sendi));
    }

    //Shapes
    private void infoShapes(CommandSender sendi) {
        List<String> info = new ArrayList<>();

        for (String shape : RtpEffectParticles.shapeTypes) {
            if (info.isEmpty() || info.size() % 2 == 0) {
                info.add("&7" + shape + "&r");
            } else
                info.add("&f" + shape + "&r");
        }

        sendi.sendMessage(Message.component(info.toString(), sendi));
    }

    //World
    public static void sendInfoWorld(CommandSender sendi, List<String> list, String label, String[] args) { //Send info
        boolean upload = Arrays.asList(args).contains("_UPLOAD_");
        list.add(0, "&e&m-----&6 BetterRTP &8| Info &e&m-----");

        String cmd = "/" + label + " " + String.join(" ", args);
        if (!upload) {
            Message.components(list, sendi).forEach(component -> sendi.sendMessage(component.asComponent()));
            if (sendi instanceof Player player) {
                player.sendMessage(Message.component("&7- &7Click to upload command log to &flogs.ronanplugins.com", player)
                        .clickEvent(ClickEvent.suggestCommand(cmd + " _UPLOAD_"))
                        .hoverEvent(HoverEvent.showText(Message.component("&6Suggested command&f: &7" + cmd + " _UPLOAD_", player))));
            } else {
                sendi.sendMessage(Component.text("Execute `" + cmd + " _UPLOAD_` to upload command log to https://logs.ronanplugins.com"));
            }
        } else {
            list.add(0, "Command: " + cmd);
            list.replaceAll(CmdInfo::stripLegacyColor);
        }
    }

    private static String stripLegacyColor(String value) {
        return value.replaceAll("&[0-9A-Fa-fK-Ok-oRr]", "");
    }

    private void infoWorld(CommandSender sendi, String label, String[] args) { //All worlds
        List<String> info = new ArrayList<>();
        for (World w : Bukkit.getWorlds())
            info.addAll(infoGetWorld(sendi, w, null, null));
        sendInfoWorld(sendi, info, label, args);
    }

    public static List<String> infoGetWorld(CommandSender sendi, World world, Player player, WorldPlayer _rtpworld) { //Specific world
        List<String> info = new ArrayList<>();
        BetterRTP pl = BetterRTP.getInstance();
        String _true = "&aTrue", _false = "&bFalse";
        info.add("&bRTP info for &7" + world.getName() + (player != null ? " &d(personalized)" : ""));
        info.add("&7- &eViewing as: &b" + (player != null ? player.getName() : "ADMIN"));
        info.add("&7- &6Allowed: " + (player != null ? PermissionCheck.getAWorld(player, world.getName()) ? _true : _false : "&cN/A"));
        if (pl.getRTP().getDisabledWorlds().contains(world.getName()) && !pl.getRTP().overriden.containsKey(world.getName())) //World disabled
            info.add("&7- &eDisabled: " + _true);
        else {
            info.add("&7- &eDisabled: " + _false);
            if (pl.getRTP().overriden.containsKey(world.getName())) { //World Overriden
                world = Bukkit.getWorld(pl.getRTP().overriden.get(world.getName()));
                info.add("&7- &6Overriden: " + _true + " &7- target `" + world.getName() + "`");
            } else
                info.add("&7- &6Overriden&7: " + _false);
            if (_rtpworld == null)
                _rtpworld = RtpHelper.getPlayerWorld(new RTPSetupInformation(world, player != null ? player : sendi, player, player != null));
            WorldDefault worldDefault = BetterRTP.getInstance().getRTP().getRTPdefaultWorld();
            info.add("&7- &eSetup Type&7: " + _rtpworld.setup_type.name() + getInfo(_rtpworld, worldDefault, "setup"));
            info.add("&7- &6Use World Border&7: " + (_rtpworld.getUseWorldborder() ? _true : _false));
            info.add("&7- &eWorld Type&7: &f" + _rtpworld.getWorldtype().name());
            info.add("&7- &6Center X&7: &f" + _rtpworld.getCenterX() + getInfo(_rtpworld, worldDefault, "centerx"));
            info.add("&7- &eCenter Z&7: &f" + _rtpworld.getCenterZ() + getInfo(_rtpworld, worldDefault, "centerz"));
            info.add("&7- &6Max Radius&7: &f" + _rtpworld.getMaxRadius() + getInfo(_rtpworld, worldDefault, "maxrad"));
            info.add("&7- &eMin Radius&7: &f" + _rtpworld.getMinRadius() + getInfo(_rtpworld, worldDefault, "minrad"));
            info.add("&7- &6Min Y&7: &f" + _rtpworld.getMinY());
            info.add("&7- &eMax Y&7: &f" + _rtpworld.getMaxY());
            info.add("&7- &6Price&7: &f" + _rtpworld.getPrice() + getInfo(_rtpworld, worldDefault, "price"));
            info.add("&7- &eCooldown&7: &f" + _rtpworld.getCooldown() + getInfo(_rtpworld, worldDefault, "cooldown"));
            info.add("&7- &6Biomes&7: &f" + _rtpworld.getBiomes().toString());
            info.add("&7- &eShape&7: &f" + _rtpworld.getShape().toString() + getInfo(_rtpworld, worldDefault, "shape"));
            info.add("&7- &6Permission Group&7: " + (_rtpworld.getConfig() != null ? "&a" + _rtpworld.getConfig().getGroupName() : "&cN/A"));
            info.add("&7- &eQueue Available&7: " + (QueueHandler.isEnabled() ? QueueHandler.getApplicableAsync(_rtpworld).size() : "&cDisabled"));
        }
        return info;
    }

    //Janky, but it works
    private static String getInfo(WorldPlayer worldPlayer, WorldDefault worldDefault, String type) {
        switch (type) {
            case "centerx":
                return worldPlayer.getUseWorldborder() || worldPlayer.getCenterX() == worldDefault.getCenterX() ? worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)" : "";
            case "centerz":
                return worldPlayer.getUseWorldborder() || worldPlayer.getCenterZ() == worldDefault.getCenterZ() ? worldPlayer.getUseWorldborder() ? " &8(worldborder)" : " &8(default)" : "";
            case "maxrad":
                return worldPlayer.getUseWorldborder() || worldPlayer.getMaxRadius() == worldDefault.getMaxRadius() ?
                        worldPlayer.getUseWorldborder() ?
                        worldPlayer.getMaxRadius() >= worldPlayer.getWorld().getWorldBorder().getSize() ?
                                " &8(worldborder)" : " &8(custom)" : " &8(default)" : "";
            case "minrad":
                return worldPlayer.getMinRadius() == worldDefault.getMinRadius() ? " &8(default)" : "";
            case "price":
                return worldPlayer.getPrice() == worldDefault.getPrice() ? " &8(default)" : "";
            case "shape":
                return worldPlayer.getShape() == worldDefault.getShape() ? " &8(default)" : "";
            case "setup":
                return worldPlayer.setup_type == RtpSetupType.LOCATION ? " &7(" + worldPlayer.setup_name + ")" : "";
            case "cooldown":
                return worldPlayer.getPlayer() != null ? PermissionNode.BYPASS_COOLDOWN.check(worldPlayer.getPlayer()) ? " &8(bypassing)" : "" : " &cN/A";
        }
        return "";
    }

    //Effects
    private void infoEffects(CommandSender sendi) {
        List<String> info = new ArrayList<>();

        for (PotionEffectType effect : PotionEffectType.values()) {
            if (info.isEmpty() || info.size() % 2 == 0) {
                info.add("&7" + effect.getName() + "&r");
            } else
                info.add("&f" + effect.getName() + "&r");
        }

        sendi.sendMessage(Message.component(info.toString(), sendi));
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> info = new ArrayList<>();
        if (args.length == 2) {
            for (CmdInfoSub cmd : CmdInfoSub.values())
                if (cmd.name().toLowerCase().startsWith(args[1].toLowerCase()))
                    info.add(cmd.name().toLowerCase());
        } else if (args.length == 3) {
            if (CmdInfoSub.WORLD.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                for (World world : Bukkit.getWorlds())
                    if (world.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                        info.add(world.getName());
            } else if (CmdInfoSub.PLAYER.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                        info.add(p.getName());
                }
            }
        } else if (args.length == 4) {
            if (CmdInfoSub.WORLD.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[3].toLowerCase()))
                        info.add(p.getName());
                }
            }
        }
        return info;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.INFO;
    }
}
