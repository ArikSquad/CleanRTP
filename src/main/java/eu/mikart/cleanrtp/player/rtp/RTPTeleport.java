package eu.mikart.cleanrtp.player.rtp;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.effects.RtpEffectTitles;
import eu.mikart.cleanrtp.player.rtp.effects.RTPEffects;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.player.events.custom.RtpTeleportEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpTeleportPostEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpTeleportPreEvent;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;

public class RTPTeleport {

    private final RTPEffects effects = new RTPEffects();

    void load() {
        effects.load();
    }

    void sendPlayer(final CommandSender sendi, final Player p, final Location location, final WorldPlayer wPlayer,
                    final int attempts, RtpType type) throws NullPointerException {
        Location oldLoc = p.getLocation();
        loadingTeleport(p, sendi); //Send loading message to player who requested
        try {
            RtpTeleportEvent event = new RtpTeleportEvent(p, location, wPlayer.getWorldtype());
            getPl().getServer().getPluginManager().callEvent(event);
            Location loc = event.getLocation();
            p.teleportAsync(loc).thenRun(new BukkitRunnable() { //Async teleport
                @Override
                public void run() {
                    afterTeleport(p, loc, wPlayer, attempts, oldLoc, type);
                    if (sendi != p) //Tell player who requested that the player rtp'd
                        sendSuccessMsg(sendi, p.getName(), loc, wPlayer, false, attempts);
                    getPl().getPInfo().getCurrentRtp().remove(p); //No longer rtp'ing
                    //Save respawn location if first join
                    if (type == RtpType.JOIN) //RTP Type was Join
                        if (BetterRTP.getInstance().getSettings().getGeneral().getRtpOnFirstJoin().isEnabled()) //Save as respawn is enabled
                            p.setRespawnLocation(loc, true);
                }
            });
        } catch (Exception e) {
            getPl().getPInfo().getCurrentRtp().remove(p); //No longer rtp'ing (errored)
            getPl().getLogger().log(java.util.logging.Level.SEVERE, "Teleport failed for " + p.getName(), e);
        }
    }

    //Effects

    public void afterTeleport(Player p, Location loc, WorldPlayer wPlayer, int attempts, Location oldLoc, RtpType type) {
        //Only a successful rtp should run this OR '/rtp test'
        effects.getSounds().playTeleport(p);
        effects.getParticles().display(p);
        effects.getPotions().giveEffects(p);
        effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.TELEPORT, p, loc, attempts, 0);
        if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.TELEPORT))
            sendSuccessMsg(p, p.getName(), loc, wPlayer, true, attempts);
        getPl().getServer().getPluginManager().callEvent(new RtpTeleportPostEvent(p, loc, oldLoc, wPlayer, type));
    }

    public boolean beforeTeleportInstant(CommandSender sendi, Player p) {
        RtpTeleportPreEvent event = new RtpTeleportPreEvent(p);
        getPl().getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            effects.getSounds().playDelay(p);
            effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.NODELAY, p, p.getLocation(), 0, 0);
            if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.NODELAY))
                MessagesCore.SUCCESS_TELEPORT.send(sendi);
        }
        return event.isCancelled();
    }

    public boolean beforeTeleportDelay(Player p, int delay) { //Only Delays should call this
        RtpTeleportPreEvent event = new RtpTeleportPreEvent(p);
        getPl().getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            effects.getSounds().playDelay(p);
            effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.DELAY, p, p.getLocation(), 0, delay);
            if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.DELAY))
                MessagesCore.DELAY.send(p, delay);
        }
        return event.isCancelled();
    }

    public void cancelledTeleport(Player p) { //Only Delays should call this
        effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.CANCEL, p, p.getLocation(), 0, 0);
        if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.CANCEL))
            MessagesCore.MOVED.send(p);
    }

    private void loadingTeleport(Player p, CommandSender sendi) {
        effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.LOADING, p, p.getLocation(), 0, 0);
        if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.LOADING) && sendStatusMessage()) { //Show msg if enabled or if not same player
            if (p == sendi)
                MessagesCore.SUCCESS_LOADING.send(sendi);
            MessagesCore.SUCCESS_LOADING.send(p);
        }
    }

    public void failedTeleport(Player p, CommandSender sendi) {
        effects.getTitles().showTitle(RtpEffectTitles.RtpTitleType.FAILED, p, p.getLocation(), 0, 0);
        if (effects.getTitles().sendMsg(RtpEffectTitles.RtpTitleType.FAILED))
            if (p == sendi)
                MessagesCore.FAILED_NOTSAFE.send(p, BetterRTP.getInstance().getRTP().maxAttempts);
            else
                MessagesCore.OTHER_NOTSAFE.send(sendi, Arrays.asList(
                        BetterRTP.getInstance().getRTP().maxAttempts,
                        p.getName()));
    }

    private void sendSuccessMsg(CommandSender sendi, String player, Location loc, WorldPlayer wPlayer, boolean sameAsPlayer, int attempts) {
        if (sameAsPlayer) {
            if (wPlayer.getPrice() == 0 || PermissionNode.BYPASS_ECONOMY.check(sendi))
                MessagesCore.SUCCESS_BYPASS.send(sendi, Arrays.asList(loc, attempts));
            else
                MessagesCore.SUCCESS_PAID.send(sendi, Arrays.asList(loc, wPlayer, attempts));
        } else
            MessagesCore.OTHER_SUCCESS.send(sendi, Arrays.asList(loc, player, attempts));
    }

    private boolean sendStatusMessage() {
        return getPl().getSettings().getGeneral().isStatusMessages();
    }

    private BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
