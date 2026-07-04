package eu.mikart.cleanrtp.player.rtp;

import lombok.Getter;
import eu.mikart.cleanrtp.references.messages.MessagesCore;

public enum RtpErrorRequestReason {
    IS_RTPING(MessagesCore.ALREADY),
    NO_PERMISSION(MessagesCore.NOPERMISSION_WORLD),
    WORLD_DISABLED(MessagesCore.DISABLED_WORLD),
    COOLDOWN(MessagesCore.COOLDOWN),
    PRICE_ECONOMY(MessagesCore.FAILED_PRICE);

    @Getter private final MessagesCore msg;

    RtpErrorRequestReason(MessagesCore msg) {
        this.msg = msg;
    }
}
