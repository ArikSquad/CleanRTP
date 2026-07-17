package eu.mikart.cleanrtp.database;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.versions.AsyncHandler;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

/** Shared SQLite lifecycle and update support for the plugin repositories. */
public abstract class SQLite {
    private static final String DATABASE_FILE = "database.db";
    private static final String SQL_ERROR = "Could not execute SQLite statement";

    protected List<String> tables = List.of();
    private final DATABASE_TYPE type;
    private volatile boolean loaded;

    protected SQLite(DATABASE_TYPE type) {
        this.type = type;
    }

    protected abstract List<String> getTables();

    protected final Connection getSQLConnection() throws SQLException {
        File directory = new File(BetterRTP.getInstance().getDataFolder(), "data");
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new SQLException("Could not create database directory " + directory);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + new File(directory, DATABASE_FILE));
    }

    public void load() {
        loaded = false;
        tables = List.copyOf(getTables());
        if (tables.isEmpty()) {
            loaded = true;
            return;
        }

        AsyncHandler.async(() -> {
            try (Connection connection = getSQLConnection(); Statement statement = connection.createStatement()) {
                for (String table : tables) {
                    statement.executeUpdate(createTableStatement(table));
                    addMissingColumns(statement, table);
                    BetterRTP.debug("Database " + type + ":" + table + " configured and loaded!");
                }
                loaded = true;
            } catch (SQLException exception) {
                BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not initialize SQLite database", exception);
            }
        });
    }

    private void addMissingColumns(Statement statement, String table) {
        for (Column column : columns()) {
            try {
                statement.executeUpdate("ALTER TABLE " + quoteIdentifier(table) + " ADD COLUMN "
                        + quoteIdentifier(column.name()) + " " + column.type());
            } catch (SQLException exception) {
                // SQLite has no ADD COLUMN IF NOT EXISTS. Ignore only duplicate-column failures.
                if (!exception.getMessage().toLowerCase().contains("duplicate column name")) {
                    BetterRTP.getInstance().getLogger().log(Level.WARNING,
                            "Could not migrate SQLite table " + table + " column " + column.name(), exception);
                }
            }
        }
    }

    private String createTableStatement(String table) {
        return "CREATE TABLE IF NOT EXISTS " + quoteIdentifier(table) + " (" + columns().stream()
                .map(column -> quoteIdentifier(column.name()) + " " + column.type())
                .reduce((left, right) -> left + ", " + right)
                .orElseThrow() + ")";
    }

    protected final boolean sqlUpdate(String sql, List<?> parameters) {
        try (Connection connection = getSQLConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, parameters);
            statement.executeUpdate();
            return true;
        } catch (SQLException exception) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, SQL_ERROR, exception);
            return false;
        }
    }

    protected static void bind(PreparedStatement statement, List<?> parameters) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            statement.setObject(index + 1, parameters.get(index));
        }
    }

    protected static String quoteIdentifier(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }

    public final boolean isLoaded() {
        return loaded;
    }

    private List<Column> columns() {
        return switch (type) {
            case PLAYERS -> from(DatabasePlayers.COLUMNS.values());
            case COOLDOWN -> from(DatabaseCooldowns.COLUMNS.values());
            case QUEUE -> from(DatabaseQueue.COLUMNS.values());
        };
    }

    private static List<Column> from(DatabasePlayers.COLUMNS[] values) {
        return java.util.Arrays.stream(values).map(value -> new Column(value.name, value.type)).toList();
    }

    private static List<Column> from(DatabaseCooldowns.COLUMNS[] values) {
        return java.util.Arrays.stream(values).map(value -> new Column(value.name, value.type)).toList();
    }

    private static List<Column> from(DatabaseQueue.COLUMNS[] values) {
        return java.util.Arrays.stream(values).map(value -> new Column(value.name, value.type)).toList();
    }

    private record Column(String name, String type) {
    }

    public enum DATABASE_TYPE {
        PLAYERS, COOLDOWN, QUEUE
    }
}
