package eu.mikart.cleanrtp.player.events.custom;

import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RtpTeleportEvent extends RTPEvent {

    Player p;
    Location loc;
    WorldType worldType;

    public RtpTeleportEvent(Player p, Location loc, WorldType worldType) {
        this.p = p;
        this.loc = loc;
        this.worldType = worldType;
    }

    public Player getPlayer() {
        return p;
    }

    public Location getLocation() {
        return loc;
    }

    public void changeLocation(Location loc) {
        this.loc = loc;
    }

    public WorldType getWorldType() {
        return worldType;
    }
}
