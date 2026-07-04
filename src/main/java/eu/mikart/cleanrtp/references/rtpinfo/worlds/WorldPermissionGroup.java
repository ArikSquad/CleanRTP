package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import lombok.Getter;
import lombok.NonNull;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WorldPermissionGroup implements RTPWorld, RtpWorldDefaulted {
    private boolean useWorldborder;
    private int centerX, centerZ, maxRad, minRad, miny, maxy;
    private float price;
    private List<String> biomes;
    public World world;
    private RtpShape shape;
    @Getter private int priority;
    @Getter private final String groupName;
    private long cooldown;

    public WorldPermissionGroup(String group, World world, Map.Entry<String, Settings.WorldOverrideSettings> fields) {
        this.groupName = group;
        this.world = world;
        setupDefaults();

        this.priority = 0;
        Settings.WorldOverrideSettings settings = fields.getValue();
        if (settings.getPriority() != null) {
            priority = settings.getPriority();
            BetterRTP.debug("- - Priority: " + priority);
        }
        settings.applyTo(this, BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled());
        validate(group);
    }

    private void validate(String group) {
        if (maxRad <= 0) {
            RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Group '" + group + "' Maximum radius of '" + maxRad + "' is not allowed! Set to default value!");
            maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }
        if (minRad < 0 || minRad >= maxRad) {
            RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Group '" + group + "' Minimum radius of '" + minRad + "' is not allowed! Set to default value!");
            minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
            if (minRad >= maxRad)
                maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }
    }

    @Override
    public boolean getUseWorldborder() {
        return useWorldborder;
    }

    @Override
    public int getCenterX() {
        return centerX;
    }

    @Override
    public int getCenterZ() {
        return centerZ;
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

    @NotNull @Override
    public @NonNull World getWorld() {
        return world;
    }

    @Override
    public RtpShape getShape() {
        return shape;
    }

    @Override
    public int getMinY() {
        return miny;
    }

    @Override
    public int getMaxY() {
        return maxy;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public void setUseWorldBorder(boolean value) {
        this.useWorldborder = value;
    }

    @Override
    public void setCenterX(int value) {
        this.centerX = value;
    }

    @Override
    public void setCenterZ(int value) {
        this.centerZ = value;
    }

    @Override
    public void setMaxRadius(int value) {
        this.maxRad = value;
    }

    @Override
    public void setMinRadius(int value) {
        this.minRad = value;
    }

    @Override
    public void setPrice(float value) {
        this.price = value;
    }

    @Override
    public void setBiomes(List<String> value) {
        this.biomes = value;
    }

    @Override
    public void setWorld(World value) {
        this.world = value;
    }

    @Override
    public void setShape(RtpShape value) {
        this.shape = value;
    }

    @Override
    public void setMinY(int value) {
        this.miny = value;
    }

    @Override
    public void setMaxY(int value) {
        this.maxy = value;
    }

    @Override
    public void setCooldown(long value) {
        this.cooldown = value;
    }
}
