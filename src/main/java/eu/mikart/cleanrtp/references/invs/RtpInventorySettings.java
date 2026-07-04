package eu.mikart.cleanrtp.references.invs;

import eu.mikart.cleanrtp.references.invs.enums.RTPInventory;
import eu.mikart.cleanrtp.references.invs.types.RTPInvBlacklist;
import eu.mikart.cleanrtp.references.invs.types.RTPInvCoordinates;
import eu.mikart.cleanrtp.references.invs.types.RTPInvMain;
import eu.mikart.cleanrtp.references.invs.types.RTPInvWorlds;

public enum RtpInventorySettings {
    MAIN(new RTPInvMain(), false),
    BLACKLIST(new RTPInvBlacklist(), true),
    COORDINATES(new RTPInvCoordinates(), true),
    WORLDS(new RTPInvWorlds(), false);

    private RTPInventory inv;
    private boolean showInMain;

    RtpInventorySettings(RTPInventory inv, boolean showInMain) {
        this.inv = inv;
        this.showInMain = showInMain;
    }

    public RTPInventory getInv() {
        return inv;
    }

    public Boolean getShowMain() {
        return showInMain;
    }

    void load(RtpInventorySettings type) {
        inv.load(type);
    }
}
