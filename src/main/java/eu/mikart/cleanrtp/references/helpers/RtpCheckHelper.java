package eu.mikart.cleanrtp.references.helpers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RtpErrorRequestReason;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownData;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownHandler;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;

public class RtpCheckHelper {

    public static RtpErrorRequestReason canRTP(Player player, CommandSender sendi, WorldPlayer pWorld, RtpPlayerInfo rtpInfo) {
        if (isRTPing(player)) { //Is RTP'ing
            return RtpErrorRequestReason.IS_RTPING;
        }
        // Not forced and has 'betterrtp.world.<world>'
        if (sendi == player && !PermissionCheck.getAWorld(sendi, pWorld.getWorld().getName())) {
            return RtpErrorRequestReason.NO_PERMISSION;
        }
        // Check disabled worlds
        if (getPl().getRTP().getDisabledWorlds().contains(pWorld.getWorld().getName())) {
            return RtpErrorRequestReason.WORLD_DISABLED;
        }
        if (rtpInfo.isCheckCooldown() && isCoolingDown(player, pWorld)) { //Is Cooling down
            return RtpErrorRequestReason.COOLDOWN;
        }
        if (rtpInfo.isTakeMoney() && !getPl().getEco().hasBalance(pWorld))
            return RtpErrorRequestReason.PRICE_ECONOMY;
        if (rtpInfo.isTakeHunger() && !getPl().getEco().hasHunger(pWorld))
            return RtpErrorRequestReason.PRICE_HUNGER;
        return null;
    }

    private static boolean isRTPing(Player player) {
        return getPl().getPInfo().getRtping().getOrDefault(player, false);
    }

    public static boolean isCoolingDown(Player player, WorldPlayer pWorld) {
        if (!applyCooldown(player))
            return false;
        return getCooldown(player, pWorld) > 0L || isLocked(player);
    }

    public static boolean isLocked(Player player) {
        return getPl().getCooldowns().locked(player);
    }

    public static long getCooldown(Player player, WorldPlayer pWorld) {
        CooldownHandler cooldownHandler = getPl().getCooldowns();
        if (!cooldownHandler.isLoaded() || !cooldownHandler.loadedPlayer(player)) { //Cooldowns have yet to download
            return 1L;
        }
        //Cooldown Data
        CooldownData cooldownData = getPl().getCooldowns().get(player, pWorld.getWorld());
        if (cooldownData != null) {
            if (cooldownData.getTime() == 0) //Global cooldown with nothing
                return 0;
            else if (isLocked(player)) { //Infinite cooldown (locked)
                return -1L;
            } else { //Normal cooldown
                return cooldownHandler.timeLeft(player, cooldownData, pWorld);
            }
        }
        return 0L;
    }

    public static boolean applyCooldown(Player player) {
        return getPl().getCooldowns().isEnabled()
                && !PermissionNode.BYPASS_COOLDOWN.check(player);
    }

    public static boolean applyDelay(Player player) {
        return getPl().getSettings().isDelayEnabled()
                && getPl().getSettings().getDelayTime() > 0
                && !PermissionNode.BYPASS_DELAY.check(player);
    }

    private static BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
