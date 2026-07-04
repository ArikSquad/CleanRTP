package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdReload implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "reload";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        BetterRTP.getInstance().reload(sender);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.RELOAD;
    }

    @Override
    public String getHelp() {
        return MessagesHelp.RELOAD.get();
    }
}
