package eu.mikart.cleanrtp.player.rtp.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.player.rtp.packets.WrapperPlayServerNamedSoundEffect;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RtpEffectSounds {

    private boolean enabled;
    private String soundTeleport, soundDelay;

    void load() {
        Settings.Effects config = BetterRTP.getInstance().getSettings().getGeneral().getEffects();
        enabled = config.isSounds();
        if (enabled) {
            soundTeleport = config.getSuccessSound();
            soundDelay = config.getDelaySound();
        }
    }

    public void playTeleport(Player p) {
        if (!enabled)
            return;
        if (soundTeleport != null) {
            playSound(p.getLocation(), p, soundTeleport);
        }
    }

    public void playDelay(Player p) {
        if (!enabled) return;
        if (soundDelay != null) {
            playSound(p.getLocation(), p, soundDelay);
        }
    }

    void playSound(Location loc, Player p, String sound) {
        if (BetterRTP.getInstance().getSettings().isProtocolLibSounds()) {
            try {
                ProtocolManager pm = ProtocolLibrary.getProtocolManager();
                WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect(pm.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT));
                packet.setSoundName(sound);
                packet.setEffectPositionX(loc.getBlockX());
                packet.setEffectPositionY(loc.getBlockY());
                packet.setEffectPositionZ(loc.getBlockZ());
                packet.sendPacket(p);
            } catch (NoClassDefFoundError | Exception e) {
                BetterRTP.getInstance().getLogger().severe("ProtocolLib Sounds is enabled in the effects.yml file, but no ProtocolLib plugin was found!");
                p.playSound(p.getLocation(), getSound(sound), 1F, 1F);
            }
        } else
            p.playSound(p.getLocation(), getSound(sound), 1F, 1F);
    }

    private @Nullable Sound getSound(@NotNull String sound) {
        try {
            return Registry.SOUNDS.get(Key.key(sound));
        } catch (IllegalArgumentException e) {
            BetterRTP.getInstance().getLogger().info("The sound '" + sound + "' is invalid!");
            return null;
        }
    }
}
