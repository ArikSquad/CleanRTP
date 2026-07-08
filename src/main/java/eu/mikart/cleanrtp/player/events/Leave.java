package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.player.HelperPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

class Leave {

    static void event(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        HelperPlayer.unload(p);
    }
}
