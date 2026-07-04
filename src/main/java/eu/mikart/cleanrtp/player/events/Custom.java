package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.player.events.custom.RtpTeleportPostEvent;
import eu.mikart.cleanrtp.references.depends.DepEssentials;

public class Custom {

    static void postRTP(RtpTeleportPostEvent e) {
        DepEssentials.setBackLocation(e.getPlayer(), e.getOldLocation());
    }

}
