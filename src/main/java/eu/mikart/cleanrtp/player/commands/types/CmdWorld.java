package eu.mikart.cleanrtp.player.commands.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpInfoHelper;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import eu.mikart.cleanrtp.references.messages.MessagesUsage;

public class CmdWorld implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "world";
    }

    //rtp world <world> <biome1, biome2...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 2) {
            World world = Bukkit.getWorld(args[1]);
            if (world == null) //Check if world has spaces instead of underscores
                world = Bukkit.getWorld(args[1].replace("_", " "));
            if (world != null)
                CmdTeleport.teleport(sender, label, world, RtpInfoHelper.getBiomes(args, 2, sender));
            else
                MessagesCore.NOTEXIST.send(sender, args[1]);
        } else
            usage(sender, label);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            for (World w : Bukkit.getWorlds()) {
                String _wName = w.getName().replace(" ", "_");
                if (w.getName().startsWith(args[1]) && !BetterRTP.getInstance().getRTP().getDisabledWorlds().contains(_wName)
                        && PermissionCheck.getAWorld(sender, _wName))
                    list.add(_wName);
            }
        } else if (args.length >= 3) {
            if (PermissionNode.BIOME.check(sender))
                RtpInfoHelper.addBiomes(list, args);
        }
        return list;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.WORLD;
    }

    public void usage(CommandSender sendi, String label) {
        MessagesUsage.WORLD.send(sendi, label);
    }

    @Override
    public String getHelp() {
        return MessagesHelp.WORLD.get();
    }
}
