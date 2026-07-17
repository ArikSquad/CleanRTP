package eu.mikart.cleanrtp.database;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.rtpinfo.QueueData;
import eu.mikart.cleanrtp.references.rtpinfo.QueueGenerator;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DatabaseQueue extends SQLite {
    public DatabaseQueue() {
        super(DATABASE_TYPE.QUEUE);
    }

    @Override
    protected List<String> getTables() {
        return List.of("Queue");
    }

    public enum COLUMNS {
        ID("id", "integer PRIMARY KEY AUTOINCREMENT"),
        X("x", "long"), Z("z", "long"), WORLD("world", "varchar(32)"), GENERATED("generated", "long");

        public final String name;
        public final String type;

        COLUMNS(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public void load() {
        if (QueueHandler.isEnabled()) super.load();
    }

    public List<QueueData> getInRange(QueueRangeData range) {
        String sql = "SELECT * FROM " + quoteIdentifier(tables.getFirst()) + " WHERE " + COLUMNS.WORLD.name
                + " = ? AND " + COLUMNS.X.name + " BETWEEN ? AND ? AND " + COLUMNS.Z.name
                + " BETWEEN ? AND ? ORDER BY RANDOM() LIMIT ?";
        List<QueueData> result = new ArrayList<>();
        try (Connection connection = getSQLConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, List.of(range.world().getName(), range.xLow(), range.xHigh(), range.zLow(), range.zHigh(),
                    QueueGenerator.queueMax + 1));
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    World world = Bukkit.getWorld(rows.getString(COLUMNS.WORLD.name));
                    if (world != null) {
                        result.add(new QueueData(new Location(world, rows.getLong(COLUMNS.X.name), 69,
                                rows.getLong(COLUMNS.Z.name)), rows.getLong(COLUMNS.GENERATED.name),
                                rows.getInt(COLUMNS.ID.name)));
                    }
                }
            }
        } catch (SQLException exception) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not query SQLite queue", exception);
        }
        return result;
    }

    public QueueData addQueue(Location location) {
        long generated = System.currentTimeMillis();
        String sql = "INSERT INTO " + quoteIdentifier(tables.getFirst()) + " (x, z, world, generated) VALUES (?, ?, ?, ?)";
        try (Connection connection = getSQLConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, List.of(location.getBlockX(), location.getBlockZ(), location.getWorld().getName(), generated));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? new QueueData(location, generated, keys.getInt(1)) : null;
            }
        } catch (SQLException exception) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not add SQLite queue entry", exception);
            return null;
        }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM " + quoteIdentifier(tables.getFirst());
        try (Connection connection = getSQLConnection(); PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rows = statement.executeQuery()) {
            return rows.next() ? rows.getInt(1) : 0;
        } catch (SQLException exception) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not count SQLite queue entries", exception);
            return 0;
        }
    }

    public boolean removeLocation(Location location) {
        String sql = "DELETE FROM " + quoteIdentifier(tables.getFirst()) + " WHERE x = ? AND z = ? AND world = ?";
        return sqlUpdate(sql, List.of(location.getBlockX(), location.getBlockZ(), location.getWorld().getName()));
    }

    public record QueueRangeData(int xLow, int xHigh, int zLow, int zHigh, World world) {
        public QueueRangeData(RTPWorld world) {
            this(world.getCenterX() - world.getMaxRadius(), world.getCenterX() + world.getMaxRadius(),
                    world.getCenterZ() - world.getMaxRadius(), world.getCenterZ() + world.getMaxRadius(), world.getWorld());
        }
    }
}
