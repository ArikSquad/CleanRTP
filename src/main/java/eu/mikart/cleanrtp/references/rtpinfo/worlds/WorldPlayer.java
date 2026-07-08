package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import lombok.Getter;
import eu.mikart.cleanrtp.commands.RtpSetupType;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpPlayerInfo;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WorldPlayer implements RTPWorld, RtpWorldDefaulted {
    private boolean useWorldborder, RTPOnDeath;
    private int CenterX, CenterZ, maxRad, minRad, min_y, max_y;
    @Setter
    private float price;
    private long cooldown;
    @Setter
    private List<String> biomes;
    @Getter private final Player player;
    @Getter private final CommandSender sendi;
    @Getter private final RtpPlayerInfo playerInfo;
    @Getter private final RtpType rtp_type;
    private final World world;
    private WorldType world_type;
    @Getter
    public WorldPermissionGroup config = null;
    @Setter
    private RtpShape shape;
    public RtpSetupType setup_type = RtpSetupType.DEFAULT;
    public String setup_name;

    @Getter private boolean setup = false;

    public WorldPlayer(RTPSetupInformation setup_info) {
        this.sendi = setup_info.getSender();
        this.player = setup_info.getPlayer();
        this.world = setup_info.getWorld();
        this.rtp_type = setup_info.getRtp_type();
        this.playerInfo = setup_info.getPlayerInfo();
    }

    public void setup(String setup_name, RTPWorld world, List<String> biomes) {
        if (world instanceof WorldLocation) {
            setup_type = RtpSetupType.LOCATION;
        } else if (world instanceof WorldCustom) {
            setup_type = RtpSetupType.CUSTOM_WORLD;
        } else if (world instanceof WorldPermissionGroup)
            setup_type = RtpSetupType.PERMISSIONGROUP;
        this.setup_name = setup_name;
        setUseWorldBorder(world.getUseWorldborder());

        setCenterX(world.getCenterX());
        setCenterZ(world.getCenterZ());
        setMaxRadius(world.getMaxRadius());
        setMinRadius(world.getMinRadius());
        setShape(world.getShape());
        if (world instanceof WorldDefault)
            setPrice(((WorldDefault) world).getPrice(getWorld().getName()));
        else
            setPrice(world.getPrice());
        List<String> list = new ArrayList<>(world.getBiomes());
        if (biomes != null) {
            list.clear();
            list.addAll(biomes);
        }
        setBiomes(list);
        //World border protection
        if (getUseWorldborder()) {
            WorldBorder border = getWorld().getWorldBorder();
            int _borderRad = (int) border.getSize() / 2;
            if (getMaxRadius() > _borderRad)
                setMaxRadius(_borderRad);
            setCenterX(border.getCenter().getBlockX());
            setCenterZ(border.getCenter().getBlockZ());
        }
        //Make sure our borders will not cause an invalid integer
        if (getMaxRadius() <= getMinRadius()) {
            setMinRadius(BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius());
            if (getMaxRadius() <= getMinRadius())
                setMinRadius(0);
        }
        //MinY
        setMinY(world.getMinY());
        setMaxY(world.getMaxY());
        //Cooldown
        setCooldown(world.getCooldown());
        setup = true;
    }

    @NotNull
    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public boolean getUseWorldborder() {
        return useWorldborder;
    }

    @Override
    public int getCenterX() {
        return CenterX;
    }

    @Override
    public int getCenterZ() {
        return CenterZ;
    }

    @Override
    public int getMaxRadius() {
        return maxRad;
    }

    @Override
    public int getMinRadius() {
        return minRad;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public List<String> getBiomes() {
        return biomes;
    }

    @Override
    public RtpShape getShape() {
        return shape;
    }

    @Override
    public void setUseWorldBorder(boolean bool) {
        useWorldborder = bool;
    }

    @Override
    public void setCenterX(int x) {
        CenterX = x;
    }

    @Override
    public void setCenterZ(int z) {
        CenterZ = z;
    }

    //Modifiable
    public void setMaxRadius(int max) {
        maxRad = max;
    }

    public void setMinRadius(int min) {
        minRad = min;
    }

    @Override
    public void setWorld(World value) {
        //Can't override this one buddy
    }

    //Custom World type
    public void setWorldtype(WorldType type) {
        this.world_type = type;
    }

    public void setMinY(int value) {
        this.min_y = value;
    }

    public void setMaxY(int value) {
        this.max_y = value;
    }

    @Override
    public void setCooldown(long value) {
        this.cooldown = value;
    }

    public WorldType getWorldtype() {
        return this.world_type;
    }

    public int getMinY() {
        return min_y;
    }

    @Override
    public int getMaxY() {
        return max_y;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

}
