package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdTeleport implements RTPCommand {

    //Label is the %command% placeholder in messages
    public static void teleport(CommandSender sendi, String label, World world, List<String> biomes) {
        if (sendi instanceof Player)
            RtpHelper.tp((Player) sendi, sendi, world, biomes, RtpType.COMMAND);
        else
            RtpMessage.sms(sendi, "Must be a player to use this command! Try '/" + label + " help'");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        teleport(sender, label, null, null);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull @Override
    public PermissionNode permission() {
        return PermissionNode.USE;
    }

    @Override
    public String getName() {
        return null;
    }

}
