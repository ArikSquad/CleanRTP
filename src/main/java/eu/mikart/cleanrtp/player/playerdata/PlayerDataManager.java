package eu.mikart.cleanrtp.player.playerdata;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerData getData(@NonNull Player p) {
        return playerData.computeIfAbsent(p.getUniqueId(), _ -> new PlayerData(p));
    }

    @Nullable
    public PlayerData getData(UUID id) {
        return playerData.get(id);
    }

    public void clear() {
        playerData.clear();
    }

    public void clear(Player p) {
        playerData.remove(p.getUniqueId());
    }
}
