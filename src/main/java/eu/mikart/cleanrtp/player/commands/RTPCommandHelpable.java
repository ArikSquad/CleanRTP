package eu.mikart.cleanrtp.player.commands;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;

public interface RTPCommandHelpable {

    ComponentLike getHelp(CommandSender sender, String label);
}
