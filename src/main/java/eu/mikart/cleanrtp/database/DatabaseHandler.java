package eu.mikart.cleanrtp.database;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;

public class DatabaseHandler {

    @Getter private final DatabasePlayers databasePlayers = new DatabasePlayers();
    @Getter private final DatabaseCooldowns databaseCooldowns = new DatabaseCooldowns();
    @Getter private final DatabaseQueue databaseQueue = new DatabaseQueue();

    public void load() {
        databasePlayers.load();
        databaseCooldowns.load();
        databaseQueue.load();
    }

    public static DatabasePlayers getPlayers() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabasePlayers();
    }

    public static DatabaseCooldowns getCooldowns() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabaseCooldowns();
    }

    public static DatabaseQueue getQueue() {
        return BetterRTP.getInstance().getDatabaseHandler().getDatabaseQueue();
    }

}
