package eu.mikart.cleanrtp.player;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.playerdata.PlayerData;
import org.bukkit.entity.Player;

public class HelperPlayer {

    public static PlayerData getData(Player p) {
        return getPl().getPlayerDataManager().getData(p);
    }

    public static void unload(Player p) {
        getPl().getPlayerDataManager().clear(p);
    }

    private static BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
