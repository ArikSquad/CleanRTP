package eu.mikart.cleanrtp.player.playerdata;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownData;

public class PlayerData {

    public boolean loading; // figure out what this is
    public final Player player;
    @Getter final ConcurrentHashMap<World, CooldownData> cooldowns = new ConcurrentHashMap<>();
    @Getter @Setter boolean rtping;
    @Getter @Setter int rtpCount;
    @Getter @Setter long globalCooldown;
    @Getter @Setter long invincibleEndTime;

    PlayerData(Player player) {
        this.player = player;
    }

    public void load(boolean joined) {}
}
