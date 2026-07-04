package eu.mikart.cleanrtp.player.events.custom;

import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RtpTeleportPostEvent extends RTPEvent {

    Player p;
    Location loc;
    Location oldLoc;
    RtpType type;
    WorldPlayer wPlayer;

    public RtpTeleportPostEvent(Player p, Location loc, Location oldLoc, WorldPlayer wPlayer, RtpType type) {
        this.p = p;
        this.loc = loc;
        this.oldLoc = oldLoc;
        this.type = type;
        this.wPlayer = wPlayer;
    }

    public Player getPlayer() {
        return p;
    }

    public Location getLocation() {
        return loc;
    }

    public Location getOldLocation() {
        return oldLoc;
    }

    public RtpType getType() {
        return type;
    }

    public WorldPlayer getWorldPlayer() {
        return wPlayer;
    }
}
