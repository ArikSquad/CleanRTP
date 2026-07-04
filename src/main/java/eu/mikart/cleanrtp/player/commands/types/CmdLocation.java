package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import eu.mikart.cleanrtp.references.messages.MessagesUsage;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdLocation implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "location";
    }

    //rtp location <location name> [player]
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                for (Map.Entry<String, RTPWorld> location : getLocations(sender, p.getWorld()).entrySet())
                    if (location.getKey().equalsIgnoreCase(args[1].toLowerCase())) {
                        RtpHelper.tp(p, sender, null, null, RtpType.COMMAND, false, false, (WorldLocation) location.getValue());
                        return;
                    }
                usage(sender, label);
            } else
                sender.sendMessage("Console is not able to execute this command! Try '/rtp help'");
        } else if (args.length == 3 && PermissionNode.RTP_OTHER.check(sender)) {
            Player p = Bukkit.getPlayer(args[2]);
            if (p != null && p.isOnline()) {
                for (Map.Entry<String, RTPWorld> location : getLocations(sender, null).entrySet()) {
                    if (location.getKey().equalsIgnoreCase(args[1].toLowerCase())) {
                        RtpHelper.tp(p, sender, null, null, RtpType.COMMAND, false, false, (WorldLocation) location.getValue());
                        return;
                    }
                }
                usage(sender, label);
            } else if (p != null)
                MessagesCore.NOTONLINE.send(sender, args[1]);
            else
                usage(sender, label);
        } else
            usage(sender, label);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            Player p = (Player) sender;
            for (String location_name : getLocations(sender, p.getWorld()).keySet())
                if (location_name.toLowerCase().startsWith(args[1].toLowerCase()))
                    list.add(location_name);
        } else if (args.length == 3 && PermissionNode.RTP_OTHER.check(sender)) {
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    list.add(p.getName());
        }
        return list;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.LOCATION;
    }

    public void usage(CommandSender sendi, String label) {
        MessagesUsage.LOCATION.send(sendi, label);
    }

    //Get locations a player has access to
    public static HashMap<String, RTPWorld> getLocations(CommandSender sendi, @Nullable World world) {
        HashMap<String, RTPWorld> locations = new HashMap<>();
        boolean needPermission = BetterRTP.getInstance().getSettings().isLocationNeedPermission();
        boolean needSameWorld = BetterRTP.getInstance().getSettings().isUseLocationsInSameWorld();
        if (needSameWorld)
            needSameWorld = !PermissionNode.BYPASS_LOCATION.check(sendi);
        for (Map.Entry<String, RTPWorld> location : BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet()) {
            boolean add = true;
            if (needPermission) //Do we need permission to go to this location?
                add = PermissionCheck.getLocation(sendi, location.getKey());
            if (add && needSameWorld) //Can be added and needs same world (if not same world, we don't check)
                add = world == null || location.getValue().getWorld().equals(world);
            if (add) //Location can be added to list
                locations.put(location.getKey(), location.getValue());
        }
        return locations;
    }

    @Override
    public net.kyori.adventure.text.ComponentLike getHelp(org.bukkit.command.CommandSender sender, String label) {
        return eu.mikart.cleanrtp.references.messages.Message.translatableRaw(sender, MessagesHelp.LOCATION.key(), label);
    }

    @Override public boolean enabled() {
        return BetterRTP.getInstance().getSettings().isLocationEnabled();
    }
}
