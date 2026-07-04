package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdHelp implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "help";
    }

    public void execute(CommandSender sender, String label, String[] args) {
        List<String> list = new ArrayList<>();
        list.add(MessagesHelp.PREFIX.get());
        list.add(MessagesHelp.MAIN.get());
        for (RTPCommand cmd : BetterRTP.getInstance().getCmd().commands)
            if (cmd.permission().check(sender))
                if (cmd instanceof RTPCommandHelpable) {
                    String help = ((RTPCommandHelpable) cmd).getHelp();
                    list.add(help);
                }
        RtpMessage.sms(sender, list, Collections.singletonList(label));
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }


    @NotNull public PermissionNode permission() {
        return PermissionNode.USE;
    }

    @Override
    public String getHelp() {
        return MessagesHelp.HELP.get();
    }
}
