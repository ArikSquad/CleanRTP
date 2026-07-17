package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.player.rtp.RtpShape;

import java.util.List;

/** Immutable effective settings used by a teleport request. */
public record RtpWorldSettings(
        boolean useWorldBorder,
        int centerX,
        int centerZ,
        int maxRadius,
        int minRadius,
        float price,
        List<String> biomes,
        RtpShape shape,
        int minY,
        int maxY,
        long cooldown
) {
    public RtpWorldSettings {
        biomes = List.copyOf(biomes);
    }

    public static RtpWorldSettings from(RTPWorld world, float price, List<String> biomes,
                                        int centerX, int centerZ, int maxRadius, int minRadius) {
        return new RtpWorldSettings(world.getUseWorldborder(), centerX, centerZ, maxRadius, minRadius, price,
                biomes, world.getShape(), world.getMinY(), world.getMaxY(), world.getCooldown());
    }
}
