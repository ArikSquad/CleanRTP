package eu.mikart.cleanrtp.references.rtpinfo.worlds;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import org.bukkit.World;

import java.util.List;

public interface RtpWorldDefaulted {

    void setUseWorldBorder(boolean value);

    void setCenterX(int value);

    void setCenterZ(int value);

    void setMaxRadius(int value);

    void setMinRadius(int value);

    void setPrice(float value);

    void setBiomes(List<String> value);

    void setWorld(World value);

    void setShape(RtpShape value);

    void setMinY(int value);

    void setMaxY(int value);

    void setCooldown(long value);

    default void setupDefaults() {
        setAllFrom(BetterRTP.getInstance().getRTP().getRTPdefaultWorld());
    }

    default void setAllFrom(RTPWorld rtpWorld) {
        setMaxRadius(rtpWorld.getMaxRadius());
        setMinRadius(rtpWorld.getMinRadius());
        setUseWorldBorder(rtpWorld.getUseWorldborder());
        setCenterX(rtpWorld.getCenterX());
        setCenterZ(rtpWorld.getCenterZ());
        setPrice(rtpWorld.getPrice());
        setBiomes(rtpWorld.getBiomes());
        setShape(rtpWorld.getShape());
        setMinY(rtpWorld.getMinY());
        setMaxY(rtpWorld.getMaxY());
        setCooldown(rtpWorld.getCooldown());
    }
}
