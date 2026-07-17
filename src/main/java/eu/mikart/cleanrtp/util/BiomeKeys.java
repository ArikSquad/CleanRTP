package eu.mikart.cleanrtp.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Biome;

public final class BiomeKeys {
    private BiomeKeys() {
    }

    public static String value(Biome biome) {
        Key key = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).getKey(biome);
        return key.asString();
    }
}
