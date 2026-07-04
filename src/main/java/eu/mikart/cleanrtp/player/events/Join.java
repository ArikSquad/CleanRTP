package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join {

    static void event(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        AsyncHandler.async(() -> {
                getPl().getCooldowns().loadPlayer(p);
            });
        rtpOnFirstJoin(p);
    }


    //RTP on first join
    private static void rtpOnFirstJoin(Player p) {
        if (getPl().getSettings().isRtpOnFirstJoin_Enabled() && !p.hasPlayedBefore())
            RtpHelper.tp(p, Bukkit.getConsoleSender(),
                    Bukkit.getWorld(getPl().getSettings().getRtpOnFirstJoin_World()),
                    null, RtpType.JOIN, true, true);
        //Fixed via @kazigk on Github
    }

    private static BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
