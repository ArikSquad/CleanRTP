package eu.mikart.cleanrtp.player.commands.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.messages.MessagesUsage;

public class CmdPlayerSudo implements RTPCommand {

    public String getName() {
        return "player_sudo";
    }

    //rtp sudoplayer <player> <world> <RtpPlayerInfo.RTP_PLAYERINFO_FLAG...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 2)
            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                RtpHelper.tp(Bukkit.getPlayer(args[1]),
                    sender,
                        Bukkit.getPlayer(args[1]).getWorld(),
                        null,
                        RtpType.FORCED,
                        null,
                        new RtpPlayerInfo(false, true, false, false, false));
            } else if (Bukkit.getPlayer(args[1]) != null)
                MessagesCore.NOTONLINE.send(sender, args[1]);
            else
                usage(sender, label);
        else if (args.length >= 3)
            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                World world = Bukkit.getWorld(args[2]);
                if (world != null) {
                    RtpHelper.tp(Bukkit.getPlayer(args[1]),
                        sender,
                            world,
                            null,
                            RtpType.FORCED,
                            null,
                            new RtpPlayerInfo(false, true, false, false, false));
                } else
                    MessagesCore.NOTEXIST.send(sender, args[2]);
            } else if (Bukkit.getPlayer(args[1]) != null)
                MessagesCore.NOTONLINE.send(sender, args[1]);
            else
                usage(sender, label);
        else
            usage(sender, label);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase()))
                    list.add(p.getName());
        } else if (args.length == 3) {
            for (World w : Bukkit.getWorlds())
                if (w.getName().startsWith(args[2]) && !BetterRTP.getInstance().getRTP().getDisabledWorlds().contains(w.getName()))
                    list.add(w.getName());
        }
        return list;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.RTP_OTHER;
    }

    public void usage(CommandSender sendi, String label) {
        MessagesUsage.RTP_OTHER.send(sendi, label);
    }
}
