package eu.mikart.cleanrtp.player.rtp;

import lombok.Getter;
import lombok.Setter;

public class RtpPlayerInfo {

    @Getter @Setter boolean
            applyDelay,
            applyCooldown,
            checkCooldown,
            takeMoney,
            takeHunger;

    public RtpPlayerInfo() {
        this(true, true, true, true, true);
    }

    public RtpPlayerInfo(boolean applyDelay, boolean applyCooldown) {
        this(applyDelay, applyCooldown, true);
    }

    public RtpPlayerInfo(boolean applyDelay, boolean applyCooldown, boolean checkCooldown) {
        this(applyDelay, applyCooldown, checkCooldown, true, true);
    }

    public RtpPlayerInfo(boolean applyDelay,
                          boolean applyCooldown,
                          boolean checkCooldown,
                          boolean takeMoney,
                          boolean takeHunger) {
        this.applyDelay = applyDelay;
        this.applyCooldown = applyCooldown;
        this.checkCooldown = checkCooldown;
        this.takeMoney = takeMoney;
        this.takeHunger = takeHunger;
    }

    public enum RTP_PLAYERINFO_FLAG {
        NODELAY, NOCOOLDOWN, IGNORECOOLDOWN, IGNOREMONEY, IGNOREHUNGER;
    }

}
