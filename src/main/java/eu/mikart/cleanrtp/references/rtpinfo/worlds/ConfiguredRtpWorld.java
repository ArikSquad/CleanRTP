package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.player.rtp.RtpShape;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** Mutable configuration object used only while loading config files. */
abstract class ConfiguredRtpWorld implements RTPWorld, RtpWorldDefaulted {
    protected boolean useWorldBorder;
    protected int centerX, centerZ, maxRadius, minRadius, minY, maxY;
    protected float price;
    protected long cooldown;
    protected List<String> biomes = new ArrayList<>();
    protected World world;
    protected RtpShape shape;

    @Override public boolean getUseWorldborder() { return useWorldBorder; }
    @Override public int getCenterX() { return centerX; }
    @Override public int getCenterZ() { return centerZ; }
    @Override public int getMaxRadius() { return maxRadius; }
    @Override public int getMinRadius() { return minRadius; }
    @Override public float getPrice() { return price; }
    @Override public List<String> getBiomes() { return biomes; }
    @NotNull @Override public World getWorld() { return world; }
    @Override public RtpShape getShape() { return shape; }
    @Override public int getMinY() { return minY; }
    @Override public int getMaxY() { return maxY; }
    @Override public long getCooldown() { return cooldown; }

    @Override public void setUseWorldBorder(boolean value) { useWorldBorder = value; }
    @Override public void setCenterX(int value) { centerX = value; }
    @Override public void setCenterZ(int value) { centerZ = value; }
    @Override public void setMaxRadius(int value) { maxRadius = value; }
    @Override public void setMinRadius(int value) { minRadius = value; }
    @Override public void setPrice(float value) { price = value; }
    @Override public void setBiomes(List<String> value) { biomes = new ArrayList<>(value); }
    @Override public void setWorld(World value) { world = value; }
    @Override public void setShape(RtpShape value) { shape = value; }
    @Override public void setMinY(int value) { minY = value; }
    @Override public void setMaxY(int value) { maxY = value; }
    @Override public void setCooldown(long value) { cooldown = value; }
}
