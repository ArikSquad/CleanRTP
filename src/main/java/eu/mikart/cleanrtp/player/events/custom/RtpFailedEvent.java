package eu.mikart.cleanrtp.player.events.custom;

import org.bukkit.entity.Player;

import lombok.Getter;
import eu.mikart.cleanrtp.player.rtp.RTPPlayer;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;

//Called when an rtp is finding a valid location
@Getter public class RtpFailedEvent extends RTPEvent {

    Player p;
    RTPWorld world;
    int attempts;

    public RtpFailedEvent(RTPPlayer rtpPlayer) {
        this.p = rtpPlayer.getPlayer();
        this.world = rtpPlayer.getWorldPlayer();
        this.attempts = rtpPlayer.getAttempts();
    }
}
