package eu.mikart.cleanrtp.player.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RtpCancelledEvent extends RTPEvent { //Called when a delayed rtp is cancelled cause player moved

    Player p;
    private static final HandlerList handler = new HandlerList();

    public RtpCancelledEvent(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }
}
