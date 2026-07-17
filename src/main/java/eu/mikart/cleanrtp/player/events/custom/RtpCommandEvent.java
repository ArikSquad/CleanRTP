package eu.mikart.cleanrtp.player.events.custom;

import eu.mikart.cleanrtp.commands.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class RtpCommandEvent extends RTPEvent implements Cancellable {

    private boolean cancelled;
    private final CommandSender sender;
    private final CommandMeta command;

    //Called before a command is executed
    public RtpCommandEvent(CommandSender sender, CommandMeta command) {
        this.sender = sender;
        this.command = command;
    }

    public CommandSender getSender() {
        return sender;
    }

    public CommandMeta getCommand() {
        return command;
    }

    /** @deprecated use {@link #getSender()} */
    @Deprecated(forRemoval = true)
    public CommandSender getSendi() {
        return sender;
    }

    /** @deprecated use {@link #getCommand()} */
    @Deprecated(forRemoval = true)
    public CommandMeta getCmd() {
        return command;
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
