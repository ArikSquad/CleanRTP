package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.references.messages.RtpMessage;

public class WorldCustom implements RTPWorld, RtpWorldDefaulted {
    public World world;
    private boolean useWorldborder, RTPOnDeath;
    private int centerX, centerZ, maxRad, minRad, miny, maxy;
    private float price;
    private long cooldown;
    private List<String> biomes;
    private RtpShape shape;

    public WorldCustom(World world) {
        this.world = world;

        //Set Defaults
        setupDefaults();

        BetterRTP.getInstance().getSettings()
                .findCustomWorld(world.getName())
                .ifPresent(settings -> settings.applyTo(this, BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled()));

        if (maxRad <= 0) {
            RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Custom world '" + world + "' Maximum radius of '" + maxRad + "' is not allowed! Set to default value!");
            maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
        }

        if (minRad < 0 || minRad >= maxRad) {
            RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Custom world '" + world + "' Minimum radius of '" + minRad + "' is not allowed! Set to default value!");
            minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
        }
    }

    public WorldCustom(World world, RTPWorld rtpWorld) {
        setAllFrom(rtpWorld);
        this.world = world;
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

    @NotNull
    @Override
    public World getWorld() {
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

    //Setters
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
