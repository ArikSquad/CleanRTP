package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdVersion implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "version";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        RtpMessage.sms(sender, "&aVersion #&e" + BetterRTP.getInstance().getDescription().getVersion());
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.VERSION;
    }

    @Override
    public String getHelp() {
        return MessagesHelp.VERSION.get();
    }
}
