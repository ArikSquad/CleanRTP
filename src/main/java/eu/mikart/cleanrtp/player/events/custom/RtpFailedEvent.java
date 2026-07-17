package eu.mikart.cleanrtp.player.events.custom;

import org.bukkit.entity.Player;

import eu.mikart.cleanrtp.player.rtp.RTPPlayer;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;

//Called when an rtp is finding a valid location
public class RtpFailedEvent extends RTPEvent {

    private final Player player;
    private final RTPWorld world;
    private final int attempts;

    public RtpFailedEvent(RTPPlayer rtpPlayer) {
        this.player = rtpPlayer.getPlayer();
        this.world = rtpPlayer.getWorldPlayer();
        this.attempts = rtpPlayer.getAttempts();
    }

    public Player getPlayer() { return player; }
    public RTPWorld getWorld() { return world; }
    public int getAttempts() { return attempts; }

    /** @deprecated use {@link #getPlayer()} */
    @Deprecated(forRemoval = true)
    public Player getP() { return player; }
}
