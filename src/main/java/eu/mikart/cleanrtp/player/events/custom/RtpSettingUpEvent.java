package eu.mikart.cleanrtp.player.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class RtpSettingUpEvent extends RTPEvent implements Cancellable {

    Player p;
    boolean cancelled = false;

    public RtpSettingUpEvent(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
