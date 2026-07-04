package eu.mikart.cleanrtp.references.rtpinfo.worlds;


import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.BetterRTP;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WorldDefault implements RTPWorld {
    private boolean useWorldborder, RTPOnDeath;
    private float price;
    private int centerX, centerZ, maxRad, minRad, miny, maxy;
    private List<String> Biomes;
    private final HashMap<String, Float> prices = new HashMap<>();
    private RtpShape shape;

    public void load() {
        BetterRTP.debug("Loading Defaults...");
        //Setups
        Settings.DefaultWorldSettings config = BetterRTP.getInstance().getSettings().getDefaultWorld();
        //Booleans
        useWorldborder = config.isUseWorldBorder();
        RTPOnDeath = config.isRtpOnDeath();
        //Integers
        centerX = config.getCenterX();
        centerZ = config.getCenterZ();
        maxRad = config.getMaxRadius();
        try {
            shape = RtpShape.valueOf(config.getShape().toUpperCase());
        } catch (Exception e) {
            shape = RtpShape.SQUARE;
        }
        if (maxRad <= 0) {
            BetterRTP.getInstance().getLogger().warning("WARNING! Default Maximum radius of '" + maxRad + "' is not allowed! Value set to '1000'");
            maxRad = 1000;
        }
        minRad = config.getMinRadius();
        if (minRad < 0 || minRad >= maxRad) {
            BetterRTP.getInstance().getLogger().warning("The Default MinRadius of '" + minRad + "' is not allowed! Value set to '0'");
            minRad = 0;
        }
        prices.clear();
        if (BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled()) {
            price = BetterRTP.getInstance().getSettings().getGeneral().getEconomy().getPrice();
            if (BetterRTP.getInstance().getSettings().isCustomWorldsEnabled()) {
                List<Map<?, ?>> world_map = BetterRTP.getInstance().getSettings().getCustomWorlds();
                for (Map<?, ?> m : world_map)
                    for (Map.Entry<?, ?> entry : m.entrySet()) {
                        String _world = entry.getKey().toString();
                        if (entry.getValue().getClass() == Integer.class)
                            prices.put(_world, Integer.parseInt((entry.getValue().toString())));
                    }
            }
        } else
            price = 0;
        //Other
        this.Biomes = config.getBiomes();
        this.miny = config.getMinY();
        if (miny > 0) {
            miny = 0;
            BetterRTP.getInstance().getLogger().warning("Warning! Default MinY value is solely for 1.17+ support, and can only be negative!");
        }
        this.maxy = config.getMaxY();
        if (maxy < 64) {
            maxy = 320;
            BetterRTP.getInstance().getLogger().warning("Warning! Default MaxY value is below water level (64)! Reset to default 320!");
        }
        //Debugger
        if (BetterRTP.getInstance().getSettings().getGeneral().isDebug()) {
            Logger log = BetterRTP.getInstance().getLogger();
            log.info("- UseWorldBorder: " + this.useWorldborder);
            log.info("- RTPOnDeath: " + this.RTPOnDeath);
            log.info("- CenterX: " + this.centerX);
            log.info("- CenterZ: " + this.centerZ);
            log.info("- MaxRadius: " + this.maxRad);
            log.info("- MinRadius: " + this.minRad);
            log.info("- Price: " + this.price);
            log.info("- MinY: " + this.miny);
            log.info("- MaxY: " + this.maxy);
            log.info("- Cooldown (default): " + getCooldown());
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

    public float getPrice(String world) {
        return prices.getOrDefault(world, getPrice());
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public List<String> getBiomes() {
        return Biomes;
    }

    @NotNull @Override
    public World getWorld() {
        return null;
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
        return BetterRTP.getInstance().getCooldowns().getDefaultCooldownTime();
    }
}
