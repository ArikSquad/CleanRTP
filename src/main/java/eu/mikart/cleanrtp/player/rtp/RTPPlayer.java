package eu.mikart.cleanrtp.player.rtp;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.events.custom.RtpFailedEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpFindLocationEvent;
import eu.mikart.cleanrtp.references.helpers.RtpCheckHelper;
import eu.mikart.cleanrtp.references.rtpinfo.QueueData;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.rtpinfo.RandomLocation;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import eu.mikart.cleanrtp.versions.AsyncHandler;

public class RTPPlayer {

    @Getter private final Player player;
    private final RTP settings;
    @Getter WorldPlayer worldPlayer;
    @Getter RtpType type;
    @Getter int attempts;
    //List<Location> attemptedLocations = new ArrayList<>();

    RTPPlayer(Player player, RTP settings, WorldPlayer worldPlayer, RtpType type) {
        this.player = player;
        this.settings = settings;
        this.worldPlayer = worldPlayer;
        this.type = type;
    }

    void randomlyTeleport(CommandSender sendi) {
        if (attempts >= settings.maxAttempts) //Cancel out, too many tries
            metMax(sendi, player);
        else { //Try again to find a safe location
            //Find a location from another Plugin
            RtpFindLocationEvent event = new RtpFindLocationEvent(this); //Find an external plugin location
            Bukkit.getServer().getPluginManager().callEvent(event);
            //Async Location finder
            if (event.isCancelled()) {
                randomlyTeleport(sendi);
                attempts++;
                return;
            }
            AsyncHandler.async(() -> {
                Location loc;
                if (event.getLocation() != null) // && WorldPlayer.checkIsValid(event.getLocation(), pWorld))
                    loc = event.getLocation();
                else {
                    QueueData queueData = QueueHandler.getRandomAsync(worldPlayer);
                    //BetterRTP.getInstance().getLogger().warning("Center x " + worldPlayer.getCenterX());
                    if (queueData != null)
                        loc = queueData.getLocation();
                    else
                        loc = RandomLocation.generateLocation(worldPlayer);
                }
                attempts++; //Add an attempt
                //Load chunk and find out if safe location (asynchronously)
                AsyncHandler.sync(() -> {
                    try { //Prior to 1.12 this async chunk will NOT work
                        CompletableFuture<Chunk> chunk = loc.getWorld().getChunkAtAsync(loc);
                        chunk.thenAccept(result -> {
                            //BetterRTP.debug("Checking location for " + p.getName());
                            attempt(sendi, loc);
                        });
                    } catch (IllegalStateException e) {
                        //Legacy non-async support
                        attempt(sendi, loc);
                    } catch (Throwable ignored) {

                    }
                });
            });
        }
    }

    private void attempt(CommandSender sendi, Location loc) {
        Location tpLoc;
        tpLoc = RandomLocation.getSafeLocation(worldPlayer.getWorldtype(), worldPlayer.getWorld(), loc, worldPlayer.getMinY(), worldPlayer.getMaxY(), worldPlayer.getBiomes());
        //attemptedLocations.add(loc);
        //Valid location?
        if (tpLoc != null && checkDepends(tpLoc)) {
            tpLoc.add(0.5, 0, 0.5); //Center location
            if (getPl().getEco().charge(player, worldPlayer)) {
                //Successfully found a safe location, set cooldown and teleport player.
                if (worldPlayer.getPlayerInfo().isApplyCooldown() && RtpCheckHelper.applyCooldown(player))
                    getPl().getCooldowns().add(player, worldPlayer.getWorld());
                tpLoc.setYaw(player.getLocation().getYaw());
                tpLoc.setPitch(player.getLocation().getPitch());
                AsyncHandler.sync(() -> settings.teleport.sendPlayer(sendi, player, tpLoc, worldPlayer, attempts, type));
            } else {
                if (worldPlayer.getPlayerInfo().applyCooldown)
                    getPl().getCooldowns().removeCooldown(player, worldPlayer.getWorld());
                getPl().getPInfo().getRtping().remove(player);
            }
        } else {
            randomlyTeleport(sendi);
            QueueHandler.remove(loc);
        }
    }

    // Compressed code for MaxAttempts being met
    private void metMax(CommandSender sendi, Player p) {
        settings.teleport.failedTeleport(p, sendi);
        getPl().getCooldowns().removeCooldown(p, worldPlayer.getWorld());
        getPl().getPInfo().getRtping().remove(p);
        //RTP Failed Event
        Bukkit.getServer().getPluginManager().callEvent(new RtpFailedEvent(this));
    }

    /**
     * @param loc Location to check
     * @return True if the location is valid
     */
    public static boolean checkDepends(Location loc) {
        return RTPPluginValidation.checkLocation(loc);
    }

    private BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}
