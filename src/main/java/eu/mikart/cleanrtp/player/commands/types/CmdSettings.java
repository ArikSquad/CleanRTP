package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdSettings implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "settings";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player)
            BetterRTP.getInstance().getInvs().getInv(RtpInventorySettings.MAIN).show((Player) sender);
        else
            RtpMessage.sms(sender, "Must be a player to use this command! Try '/" + label + " help'");
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.SETTINGS;
    }

    @Override
    public String getHelp() {
        return MessagesHelp.SETTINGS.get();
    }
}
