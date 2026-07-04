package eu.mikart.cleanrtp.player.events.custom;

import eu.mikart.cleanrtp.player.commands.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RtpCommandEvent extends RTPEvent implements Cancellable {

    boolean cancelled;
    CommandSender sendi;
    CommandMeta cmd;
    private static final HandlerList handler = new HandlerList();

    //Called before a command is executed
    public RtpCommandEvent(CommandSender sendi, CommandMeta cmd) {
        this.sendi = sendi;
        this.cmd = cmd;
    }

    public CommandSender getSendi() {
        return sendi;
    }

    public CommandMeta getCmd() {
        return cmd;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
