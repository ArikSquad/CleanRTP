package eu.mikart.cleanrtp.references.player.playerdata;

import lombok.Getter;
import lombok.Setter;
import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;

public class PlayerDataMenus {

    @Getter @Setter private Inventory inv;
    @Getter @Setter RtpInventorySettings invType;
    @Getter @Setter World invWorld;
    @Getter @Setter RtpInventorySettings invNextInv;

}
