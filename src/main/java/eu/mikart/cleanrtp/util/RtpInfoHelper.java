package eu.mikart.cleanrtp.util;

import java.util.ArrayList;
import java.util.List;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.MessagesCore;

public class RtpInfoHelper {

    // Custom biomes
    public static List<String> getBiomes(String[] args, int start, CommandSender sender) {
        List<String> biomes = new ArrayList<>();
        boolean sentError = false;
        if (PermissionNode.BIOME.check(sender))
            for (int i = start; i < args.length; i++) {
                String str = args[i];
                try {
                    biomes.add(RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(Key.key(str)).name()); // TODO: pass as a Biome
                } catch (Exception e) {
                    if (!sentError) {
                        MessagesCore.OTHER_BIOME.send(sender, str);
                        sentError = true;
                    }
                }
            }
        return biomes;
    }

}
