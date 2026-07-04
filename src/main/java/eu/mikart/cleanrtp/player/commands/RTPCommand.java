package eu.mikart.cleanrtp.player.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.references.PermissionCheck;

public interface RTPCommand {

    void execute(CommandSender sender, String label, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);

    @NotNull PermissionCheck permission();

    String getName();

    default boolean isDebugOnly() {
        return false;
    }

    default boolean enabled() {
        return true;
    };
}
