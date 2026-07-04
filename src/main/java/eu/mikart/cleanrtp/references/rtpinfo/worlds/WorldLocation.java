package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldLocation implements RTPWorld, RtpWorldDefaulted {
    private boolean useWorldborder;
    private int centerX, centerZ, maxRad, minRad, miny, maxy;
    private float price;
    private long cooldown;
    private List<String> biomes;
    private World world;
    private RtpShape shape;
    private final String name;

    public WorldLocation(String location_name) {
        Map<String, Settings.LocationEntry> map = BetterRTP.getInstance().getSettings().getLocations().entries;
        //WorldDefault worldDefault = BetterRTP.getInstance().getRTP().defaultWorld;

        setupDefaults();
        this.name = location_name;

        BetterRTP.debug("- Loading Location " + location_name + ":");
        //Find Location and cache its values
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            if (!key.equals(location_name))
                continue;
            Settings.LocationEntry section = map.get(key);
            if (section == null)
                continue;
            if (section.world != null) {
                world = Bukkit.getWorld(section.world);
            }
            if (world == null) {
                BetterRTP.getInstance().getLogger().warning("Location `" + location_name + "` does NOT have a `World` or world doesnt exist!");
                return;
            }
            useWorldborder = section.useWorldBorder;
            centerX = section.centerX;
            centerZ = section.centerZ;
            maxRad = section.maxRadius;
            if (maxRad <= 0) {
                RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Location '" + location_name + "' Maximum radius of '" + maxRad + "' is not allowed! Set to default value!");
                maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
            }

            minRad = section.minRadius;
            if (minRad < 0 || minRad >= maxRad) {
                RtpMessage.sms(Bukkit.getConsoleSender(),
                    "WARNING! Location '" + location_name + "' Minimum radius of '" + minRad + "' is not allowed! Set to default value!");
                minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
                if (minRad >= maxRad) {
                    maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
                }
            }

            this.biomes = section.biomes;

            if (BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled()) {
                if (section.price > 0) {
                    this.price = section.price;
                }
            }

            this.shape = RtpShape.valueOf(section.shape.toUpperCase());
            this.miny = section.minY;
            this.maxy = section.maxY;
            this.cooldown = section.cooldown;
        }
    }


    public boolean isValid() {
        return world != null;
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

    @Nullable
    @Override
    public String getID() {
        return name;
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
