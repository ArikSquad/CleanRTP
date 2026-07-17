package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandAfterEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandEvent;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/** Permission checks and lifecycle events shared by all command handlers. */
final class CommandExecutionService {
    private CommandExecutionService() {
    }

    static void run(CommandSender sender, CommandMeta command, Runnable action) {
        if (!PermissionNode.USE.check(sender)) {
            MessagesCore.NOPERMISSION.send(sender, PermissionNode.USE);
            return;
        }
        if (!command.permission().check(sender)) {
            MessagesCore.NOPERMISSION.send(sender, command.permission());
            return;
        }

        RtpCommandEvent event = new RtpCommandEvent(sender, command);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        BetterRTP.debug(sender.getName() + " executed: /betterrtp " + command.name());
        action.run();
        Bukkit.getPluginManager().callEvent(new RtpCommandAfterEvent(sender, command));
    }
}
