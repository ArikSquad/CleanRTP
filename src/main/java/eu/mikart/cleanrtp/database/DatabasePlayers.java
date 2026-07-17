package eu.mikart.cleanrtp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.playerdata.PlayerData;

public class DatabasePlayers extends SQLite {

    public DatabasePlayers() {
        super(DATABASE_TYPE.PLAYERS);
    }

    @Override
    public List<String> getTables() {
        List<String> list = new ArrayList<>();
        list.add("Players");
        return list;
    }

    public enum COLUMNS {
        UUID("uuid", "varchar(32) PRIMARY KEY"),
        //COOLDOWN DATA
        COUNT("count", "long"),
        LAST_COOLDOWN_DATE("last_rtp_date", "long"),
        //USES("uses", "integer"),
        ;

        public final String name;
        public final String type;

        COLUMNS(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public void setupData(PlayerData data) {
        try (Connection conn = getSQLConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + quoteIdentifier(tables.getFirst())
                     + " WHERE " + COLUMNS.UUID.name + " = ?")) {
            ps.setString(1, data.player.getUniqueId().toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data.setRtpCount(Math.toIntExact(rs.getLong(COLUMNS.COUNT.name)));
                    data.setGlobalCooldown(rs.getLong(COLUMNS.LAST_COOLDOWN_DATE.name));
                }
            }
        } catch (SQLException ex) {
            BetterRTP.getInstance().getLogger().log(Level.SEVERE, "Could not load SQLite player data", ex);
        }
    }

    //Set a player Cooldown
    public void setData(PlayerData data) {
        String pre = "INSERT OR REPLACE INTO ";
        String sql = pre + tables.get(0) + " ("
                + COLUMNS.UUID.name + ", "
                + COLUMNS.COUNT.name + ", "
                + COLUMNS.LAST_COOLDOWN_DATE.name + " "
                //+ COLUMNS.USES.name + " "
                + ") VALUES(?, ?, ?)";
        List<Object> params = List.of(data.player.getUniqueId().toString(), data.getRtpCount(), data.getGlobalCooldown());
        sqlUpdate(sql, params);
    }
}
