package eu.mikart.cleanrtp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownData;

public class DatabaseCooldowns extends SQLite {

    public DatabaseCooldowns() {
        super(DATABASE_TYPE.COOLDOWN);
    }

    @Override
    public List<String> getTables() {
        List<String> list = new ArrayList<>();

        // Ignore loaded world names if cooldowns are disabled
        if (!BetterRTP.getInstance().getCooldowns().isEnabled())
            return list;

        // Get list of disabled worlds and ensure list isn't null
        List<String> disabledWorlds = BetterRTP.getInstance().getRTP().getDisabledWorlds();
        if (disabledWorlds == null) disabledWorlds = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (!disabledWorlds.contains(world.getName())) list.add(world.getName());
        }

        return list;
    }

    public enum COLUMNS {
        UUID("uuid", "varchar(32) PRIMARY KEY"),
        //COOLDOWN DATA
        COOLDOWN_DATE("date", "long"),
        //USES("uses", "integer"),
        ;

        public final String name;
        public final String type;

        COLUMNS(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public void removePlayer(UUID uuid, World world) {
        // Create SQL query string with backtick-ed table name to allow for special characters
        String sql = String.format(
                "DELETE FROM `%s` WHERE %s = ?",
                world.getName(),
                COLUMNS.UUID.name
        );
        sqlUpdate(sql, List.of(uuid.toString()));
    }

    public CooldownData getCooldown(UUID uuid, World world) {
        try (Connection conn = getSQLConnection();
            // Create prepared statement with backtick-ed table name to allow for special characters
             PreparedStatement ps = conn.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE %s = ?",
                    world.getName(),
                    COLUMNS.UUID.name
            ))) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new CooldownData(uuid, rs.getLong(COLUMNS.COOLDOWN_DATE.name));
            }
        } catch (SQLException ex) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not load SQLite cooldown", ex);
        }
        return null;
    }

    //Set a player Cooldown
    public void setCooldown(World world, CooldownData data) {
        String sql = "INSERT OR REPLACE INTO " + quoteIdentifier(world.getName()) + " ("
                + COLUMNS.UUID.name + ", "
                + COLUMNS.COOLDOWN_DATE.name + " "
                + ") VALUES(?, ?)";
        sqlUpdate(sql, List.of(data.getUuid().toString(), data.getTime()));
    }

}
