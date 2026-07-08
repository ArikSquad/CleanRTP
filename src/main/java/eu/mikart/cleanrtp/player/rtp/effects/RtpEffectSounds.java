package eu.mikart.cleanrtp.player.rtp.effects;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
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
        p.playSound(loc, getSound(sound), 1F, 1F);
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
