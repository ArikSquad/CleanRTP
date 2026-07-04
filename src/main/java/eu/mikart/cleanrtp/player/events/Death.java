package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Death {

    static void respawnEvent(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        WorldPlayer worldPlayer = RtpHelper.getPlayerWorld(new RTPSetupInformation(
                p.getWorld(),
                p, p, false
        ));
        if (worldPlayer.getRTPOnDeath()) {
            RtpHelper.tp(p, p, p.getWorld(), null, RtpType.FORCED, true, true);
        }
    }
}
