package eu.mikart.cleanrtp.player.rtp;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RTPSetupInformation {
    //Will provide information to setup an RTP attempt
    @Getter @Setter private World world;
    @Getter @NonNull private final CommandSender sender;
    @Getter @Nullable private final Player player;
    @Getter private final boolean personalized;
    @Getter @Setter @Nullable private List<String> biomes;
    @Getter @Setter @Nullable private WorldLocation location;
    @Getter @Nullable private final RtpType rtp_type;
    @Getter private final RtpPlayerInfo playerInfo;

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized) {
        this(world, sender, player, personalized, null, false, null, null);
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                                boolean delay,
                               @Nullable RtpType rtp_type,
                               @Nullable WorldLocation location) {
        this(world, sender, player, personalized, biomes, delay, rtp_type, location, true);
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                               boolean delay,
                               @Nullable RtpType rtp_type,
                               @Nullable WorldLocation location,
                               boolean cooldown) {
        this(world, sender, player, personalized, biomes, rtp_type, location, new RtpPlayerInfo(delay, cooldown));
    }

    public RTPSetupInformation(@Nullable World world,
                               @NonNull CommandSender sender,
                               @Nullable Player player,
                               boolean personalized,
                               @Nullable List<String> biomes,
                               @Nullable RtpType rtp_type,
                               @Nullable WorldLocation location,
                               RtpPlayerInfo playerInfo) {
        this.world = world;
        this.sender = sender;
        this.player = player;
        this.personalized = personalized;
        this.biomes = biomes;
        this.rtp_type = rtp_type;
        this.location = location;
        if (this.world == null) {
            if (player != null)
                this.world = player.getWorld();
        }
        this.playerInfo = playerInfo;
    }
}
