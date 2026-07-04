package eu.mikart.cleanrtp.references.invs;

import eu.mikart.cleanrtp.references.invs.enums.RtpInventoryDefaults;
import eu.mikart.cleanrtp.BetterRTP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RTPInventories {

    private final HashMap<RtpInventorySettings, RtpInventoryDefaults> invs = new HashMap<>();

    public void load() {
        invs.clear();
        for (RtpInventorySettings type : RtpInventorySettings.values()) {
            type.load(type);
            invs.put(type, type.getInv());
        }
    }

    public void closeAll() {
        BetterRTP main = BetterRTP.getInstance();
        for (Player p : Bukkit.getOnlinePlayers())
            if (main.getPInfo().playerExists(p)) {
                main.getPInfo().clearInvs(p);
                p.closeInventory();
            }
    }

    public RtpInventoryDefaults getInv(RtpInventorySettings type) {
        return invs.get(type);
    }
}