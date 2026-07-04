package eu.mikart.cleanrtp.player;

import java.util.HashMap;

import org.bukkit.entity.Player;

import lombok.Getter;

public class PlayerInfo {

    @Getter
    private final HashMap<Player, Boolean> currentRtp = new HashMap<>();


    private void unloadAll() {
        currentRtp.clear();
    }

    private void unload(Player p) {
        currentRtp.remove(p);
    }
}
