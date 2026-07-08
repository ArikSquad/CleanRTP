package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.BetterRTP;
import lombok.Getter;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class CommandRegistrar {

    private final BetterRTP plugin;
    @Getter private Lamp<BukkitCommandActor> lamp;

    public CommandRegistrar(BetterRTP plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        lamp = BukkitLamp.builder(plugin).build();
        lamp.register(new RTPCommands());
    }
}
