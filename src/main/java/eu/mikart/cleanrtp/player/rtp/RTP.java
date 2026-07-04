package eu.mikart.cleanrtp.player.rtp;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.events.custom.RtpSettingUpEvent;
import eu.mikart.cleanrtp.references.file.FileOther;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.helpers.RtpCheckHelper;
import eu.mikart.cleanrtp.references.rtpinfo.PermissionGroup;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldDefault;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;

public class RTP {

    @Getter final RTPTeleport teleport = new RTPTeleport();
    //Cache
    public final HashMap<String, String> overriden = new HashMap<>();
    @Getter List<String> disabledWorlds, blockList;
    int maxAttempts, delayTime;
    boolean cancelOnMove, cancelOnDamage;
    public final HashMap<String, WorldType> world_type = new HashMap<>();
    //Worlds
    @Getter private final WorldDefault RTPdefaultWorld = new WorldDefault();
    @Getter private final HashMap<String, RTPWorld> RTPcustomWorld = new HashMap<>();
    @Getter private final HashMap<String, RTPWorld> RTPworldLocations = new HashMap<>();
    @Getter private final HashMap<String, PermissionGroup> permissionGroups = new HashMap<>();

    public void load() {
        FileOther.Filetype config = FileOther.Filetype.CONFIG;
        disabledWorlds = config.getStringList("DisabledWorlds");
        maxAttempts = config.getInt("Settings.MaxAttempts");
        delayTime = config.getInt("Settings.Delay.Time");
        cancelOnMove = config.getBoolean("Settings.Delay.CancelOnMove");
        cancelOnDamage = config.getBoolean("Settings.Delay.CancelOnDamage");
        blockList = config.getStringList("BlacklistedBlocks");
        //Overrides
        RTPLoader.loadOverrides(overriden);
        //WorldType
        RTPLoader.loadWorldTypes(world_type);
        //Worlds & CustomWorlds
        loadWorlds();
        //Locations
        loadLocations();
        //Permissions
        loadPermissionGroups();
        teleport.load(); //Load teleporting stuff
    }

    public void loadWorlds() { //Keeping this here because of the edit command
        RTPLoader.loadWorlds(RTPdefaultWorld, RTPcustomWorld);
    }

    public void loadLocations() { //Keeping this here because of the edit command
        RTPLoader.loadLocations(RTPworldLocations);
    }

    public void loadPermissionGroups() { //Keeping this here because of the edit command
        RTPLoader.loadPermissionGroups(permissionGroups);
    }

    public void start(RTPSetupInformation setup_info) {
        start(RtpHelper.getPlayerWorld(setup_info));
    }

    public void start(WorldPlayer pWorld) {
        RtpSettingUpEvent setup = new RtpSettingUpEvent(pWorld.getPlayer());
        Bukkit.getPluginManager().callEvent(setup);
        if (setup.isCancelled())
            return;
        rtp(pWorld.getSendi(), pWorld, pWorld.getRtp_type());
    }

    private void rtp(CommandSender sendi, WorldPlayer pWorld, RtpType type) {
        //Cooldown
        Player p = pWorld.getPlayer();
        getPl().getPInfo().getRtping().put(p, true); //Cache player so they cant run '/rtp' again while rtp'ing
        //Setup player rtp methods
        RTPPlayer rtpPlayer = new RTPPlayer(p, this, pWorld, type);
        // Delaying? Else, just go
        if (pWorld.getPlayerInfo().applyDelay && RtpCheckHelper.applyDelay(pWorld.getPlayer())) {
            new RTPDelay(sendi, rtpPlayer, delayTime, cancelOnMove, cancelOnDamage);
        } else {
            if (!teleport.beforeTeleportInstant(sendi, p))
                rtpPlayer.randomlyTeleport(sendi);
        }
    }

    private BetterRTP getPl() {
        return BetterRTP.getInstance();
    }
}