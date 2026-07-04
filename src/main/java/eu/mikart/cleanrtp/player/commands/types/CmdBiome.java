package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpInfoHelper;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import eu.mikart.cleanrtp.references.messages.MessagesUsage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CmdBiome implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "biome";
    }

    //rtp biome <biome1, biome2...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 2)
            CmdTeleport.teleport(sender, label, null, RtpInfoHelper.getBiomes(args, 1, sender));
        else
            usage(sender, label);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length >= 2)
            RtpInfoHelper.addBiomes(list, args);
        return list;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.BIOME;
    }

    public void usage(CommandSender sendi, String label) {
        MessagesUsage.BIOME.send(sendi, label);
    }

    @Override
    public net.kyori.adventure.text.ComponentLike getHelp(org.bukkit.command.CommandSender sender, String label) {
        return eu.mikart.cleanrtp.references.messages.Message.translatableRaw(sender, MessagesHelp.BIOME.key(), label);
    }
}
