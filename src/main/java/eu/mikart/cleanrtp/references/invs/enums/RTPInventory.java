package eu.mikart.cleanrtp.references.invs.enums;

import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;

public abstract class RTPInventory implements RtpInventoryDefaults {

    public RtpInventorySettings type;

    public void load(RtpInventorySettings type) {
        this.type = type;
    }
}
