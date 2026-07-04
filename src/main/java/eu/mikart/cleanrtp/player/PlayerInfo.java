package eu.mikart.cleanrtp.player;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;

public class PlayerInfo {

    private final HashMap<Player, Inventory> invs = new HashMap<>();
    //private final HashMap<Player, RtpInventorySettings> invType = new HashMap<>();
    @Getter private final HashMap<Player, World> invWorld = new HashMap<>();
    @Getter private final HashMap<Player, RtpInventorySettings> invNextInv = new HashMap<>();
    //private final HashMap<Player, CooldownData> cooldown = new HashMap<>();
    @Getter private final HashMap<Player, Boolean> rtping = new HashMap<>();
    //private final HashMap<Player, List<Location>> previousLocations = new HashMap<>();
    //private final HashMap<Player, RtpType> rtpType = new HashMap<>();

    /*private void setInv(Player p, Inventory inv) {
        invs.put(p, inv);
    }*/

    /*private void setInvType(Player p, RtpInventorySettings type) {
        invType.put(p, type);
    }*/

    public void setInvWorld(Player p, World type) {
        invWorld.put(p, type);
    }

    public void setNextInv(Player p, RtpInventorySettings type) {
        invNextInv.put(p, type);
    }

    //--Logic--

    public Boolean playerExists(Player p) {
        return invs.containsKey(p);
    }

    private void unloadAll() {
        invs.clear();
        //invType.clear();
        invWorld.clear();
        invNextInv.clear();
        //cooldown.clear();
        rtping.clear();
        //previousLocations.clear();
    }

    private void unload(Player p) {
        clearInvs(p);
        //cooldown.remove(p);
        rtping.remove(p);
        //previousLocations.remove(p);
    }

    public void clearInvs(Player p) {
        invs.remove(p);
        //invType.remove(p);
        invWorld.remove(p);
        invNextInv.remove(p);
    }
}
