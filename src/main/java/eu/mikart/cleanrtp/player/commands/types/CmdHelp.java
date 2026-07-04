package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CmdHelp implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "help";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        List<ComponentLike> list = new ArrayList<>();
        list.add(MessagesHelp.PREFIX.getComponent());
        list.add(Message.translatableRaw(sender, MessagesHelp.MAIN.key(), label));
        for (RTPCommand cmd : BetterRTP.getInstance().getCmd().commands)
            if (cmd.permission().check(sender))
                if (cmd instanceof RTPCommandHelpable) {
                    ComponentLike help = ((RTPCommandHelpable) cmd).getHelp(sender, label);
                    list.add(help);
                }
        RtpMessage.sms(sender, list);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }


    @NotNull public PermissionNode permission() {
        return PermissionNode.USE;
    }

    @Override
    public net.kyori.adventure.text.ComponentLike getHelp(org.bukkit.command.CommandSender sender, String label) {
        return eu.mikart.cleanrtp.references.messages.Message.translatableRaw(sender, MessagesHelp.HELP.key(), label);
    }
}
