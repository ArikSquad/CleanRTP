package eu.mikart.cleanrtp.player.events.custom;

import org.bukkit.command.CommandSender;

import eu.mikart.cleanrtp.player.commands.CommandMeta;

public class RtpCommandAfterEvent extends RtpCommandEvent {

    //Executed after a command was executed
    public RtpCommandAfterEvent(CommandSender sendi, CommandMeta cmd) {
        super(sendi, cmd);
    }
}
