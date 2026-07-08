package eu.mikart.cleanrtp.player.events;

import eu.mikart.cleanrtp.player.HelperPlayer;
import eu.mikart.cleanrtp.player.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport {

    static void tpEvent(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        PlayerData data = HelperPlayer.getData(p);
        //Add data to database
    }
}
