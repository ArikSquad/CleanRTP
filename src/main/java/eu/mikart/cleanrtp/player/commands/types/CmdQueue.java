package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.rtpinfo.QueueData;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CmdQueue implements RTPCommand {

    public String getName() {
        return "queue";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        Player p = (Player) sender;
        //sendi.sendMessage("Loading...");
        World world = args.length > 1 ? Bukkit.getWorld(args[1]) : null;
        AsyncHandler.async(() -> {
            if (world != null) {
                sendInfo(sender, queueGetWorld(p, world), label, args);
            } else
                queueWorlds(p, label, args);
        });
    }

    //World
    public static void sendInfo(CommandSender sendi, List<String> list, String label, String[] args) { //Send info
        boolean upload = Arrays.asList(args).contains("_UPLOAD_");
        list.add(0, "&e&m-----&6 BetterRTP &8| Queue &e&m-----");
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
            list.replaceAll(CmdQueue::stripLegacyColor);
        }
    }

    private static String stripLegacyColor(String value) {
        return value.replaceAll("&[0-9A-Fa-fK-Ok-oRr]", "");
    }

    private void queueWorlds(Player p, String label, String[] args) { //All worlds
        List<String> info = new ArrayList<>();
        int locs = 0;
        for (World w : Bukkit.getWorlds()) {
            List<String> list = queueGetWorld(p, w);
            info.addAll(list);
            locs += list.size();
        }
        info.add("&eTotal of &a%amount% &egenerated locations".replace("%amount%", String.valueOf(locs)));
        sendInfo(p, info, label, args);
    }

    private static List<String> queueGetWorld(Player player, World world) { //Specific world
        List<String> info = new ArrayList<>();
        info.add("&eWorld: &6" + world.getName());
        RTPSetupInformation setup_info = new RTPSetupInformation(RtpHelper.getActualWorld(player, world), player, player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setup_info);
        for (QueueData queue : QueueHandler.getApplicableAsync(pWorld)) {
            String str = "&8- &7x= &b%x, &7z= &b%z";
            Location loc = queue.getLocation();
            str = str.replace("%x", String.valueOf(loc.getBlockX())).replace("%z", String.valueOf(loc.getBlockZ()));
            info.add(str);
        }
        return info;
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> info = new ArrayList<>();
        if (args.length == 2) {
            for (World world : Bukkit.getWorlds())
                if (world.getName().startsWith(args[1]))
                    info.add(world.getName());
        }
        return info;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.ADMIN;
    }
}
