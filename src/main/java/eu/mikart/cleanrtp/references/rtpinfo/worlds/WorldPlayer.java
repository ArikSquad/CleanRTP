package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.commands.RtpSetupType;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** Runtime teleport context plus an immutable snapshot of its effective world settings. */
@Getter
public final class WorldPlayer implements RTPWorld {
    private final Player player;
    private final CommandSender sender;
    private final RtpPlayerInfo playerInfo;
    private final RtpType rtpType;
    private final World world;
    private WorldType worldType;
    private WorldPermissionGroup config;
    private RtpSetupType setupType = RtpSetupType.DEFAULT;
    private String setupName;
    private RtpWorldSettings settings;

    public WorldPlayer(RTPSetupInformation setupInformation) {
        sender = setupInformation.getSender();
        player = setupInformation.getPlayer();
        world = setupInformation.getWorld();
        rtpType = setupInformation.getRtp_type();
        playerInfo = setupInformation.getPlayerInfo();
    }

    public void setup(String name, RTPWorld source, List<String> biomeOverride) {
        setupType = switch (source) {
            case WorldLocation ignored -> RtpSetupType.LOCATION;
            case WorldCustom ignored -> RtpSetupType.CUSTOM_WORLD;
            case WorldPermissionGroup group -> {
                config = group;
                yield RtpSetupType.PERMISSIONGROUP;
            }
            default -> RtpSetupType.DEFAULT;
        };
        setupName = name;

        int centerX = source.getCenterX();
        int centerZ = source.getCenterZ();
        int maxRadius = source.getMaxRadius();
        int minRadius = source.getMinRadius();
        if (source.getUseWorldborder()) {
            WorldBorder border = world.getWorldBorder();
            maxRadius = Math.min(maxRadius, (int) border.getSize() / 2);
            centerX = border.getCenter().getBlockX();
            centerZ = border.getCenter().getBlockZ();
        }
        if (maxRadius <= minRadius) {
            minRadius = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
            if (maxRadius <= minRadius) minRadius = 0;
        }

        float price = source instanceof WorldDefault defaultWorld
                ? defaultWorld.getPrice(world.getName()) : source.getPrice();
        List<String> biomes = biomeOverride == null
                ? new ArrayList<>(source.getBiomes()) : new ArrayList<>(biomeOverride);
        settings = RtpWorldSettings.from(source, price, biomes, centerX, centerZ, maxRadius, minRadius);
    }

    public boolean isSetup() {
        return settings != null;
    }

    public void setWorldtype(WorldType worldType) {
        this.worldType = worldType;
    }

    public void setConfig(WorldPermissionGroup config) {
        this.config = config;
    }

    // Compatibility aliases retained while internal code migrates to conventional names.
    public CommandSender getSendi() { return sender; }
    public RtpType getRtp_type() { return rtpType; }
    public WorldType getWorldtype() { return worldType; }
    public RtpSetupType getSetup_type() { return setupType; }
    public String getSetup_name() { return setupName; }

    @NotNull @Override public World getWorld() { return world; }
    @Override public boolean getUseWorldborder() { return settings.useWorldBorder(); }
    @Override public int getCenterX() { return settings.centerX(); }
    @Override public int getCenterZ() { return settings.centerZ(); }
    @Override public int getMaxRadius() { return settings.maxRadius(); }
    @Override public int getMinRadius() { return settings.minRadius(); }
    @Override public float getPrice() { return settings.price(); }
    @Override public List<String> getBiomes() { return settings.biomes(); }
    @Override public RtpShape getShape() { return settings.shape(); }
    @Override public int getMinY() { return settings.minY(); }
    @Override public int getMaxY() { return settings.maxY(); }
    @Override public long getCooldown() { return settings.cooldown(); }
}
