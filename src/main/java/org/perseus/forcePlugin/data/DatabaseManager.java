package org.perseus.forcePlugin.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.perseus.forcePlugin.ForcePlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private final ForcePlugin plugin;
    private Connection connection;
    private final Gson gson = new Gson();

    public DatabaseManager(ForcePlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized boolean connect() {
        return connect(1);
    }

    public synchronized boolean connect(int maxRetries) {
        File dataFolder = new File(plugin.getDataFolder(), "playerdata.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create database file!", e);
            }
        }

        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return true;
                }
            } catch (SQLException e) {
                // connection is stale, proceed to reconnect
            }
        }

        int attempts = 0;
        while (attempts < maxRetries) {
            attempts++;
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
                plugin.getLogger().info("Successfully connected to the SQLite database.");
                initializeDatabase();
                return true;
            } catch (SQLException | ClassNotFoundException e) {
                if (attempts < maxRetries) {
                    plugin.getLogger().warning("Failed to connect to database (attempt " + attempts + "/" + maxRetries + "). Retrying in 2 seconds...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    plugin.getLogger().log(Level.SEVERE, "Failed to connect to the SQLite database after " + maxRetries + " attempts!", e);
                }
            }
        }
        return false;
    }

    public synchronized void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Disconnected from the SQLite database.");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to disconnect from the SQLite database!", e);
        }
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS force_users ("
                + "uuid TEXT PRIMARY KEY NOT NULL,"
                + "side TEXT NOT NULL,"
                + "active_ability TEXT,"
                + "force_level INTEGER NOT NULL,"
                + "force_xp REAL NOT NULL,"
                + "force_points INTEGER NOT NULL,"
                + "unlocked_abilities TEXT NOT NULL,"
                + "unlocked_passives TEXT,"
                + "slot_binds TEXT"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            addColumnIfNotExists("unlocked_passives", "TEXT");
            addColumnIfNotExists("slot_binds", "TEXT");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not create or update database table!", e);
        }
    }

    private void addColumnIfNotExists(String columnName, String columnType) {
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getColumns(null, null, "force_users", columnName);
            if (!rs.next()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("ALTER TABLE force_users ADD COLUMN " + columnName + " " + columnType + ";");
                    plugin.getLogger().info("Added new database column: " + columnName);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not add column " + columnName + " to database!", e);
        }
    }

    public synchronized ForceUser loadPlayerData(UUID uuid) {
        connect();
        String sql = "SELECT * FROM force_users WHERE uuid = ?";
        ForceUser forceUser = new ForceUser(uuid);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                forceUser.setSide(ForceSide.valueOf(rs.getString("side")));
                forceUser.setForceLevel(rs.getInt("force_level"));
                forceUser.setForceXp(rs.getDouble("force_xp"));
                forceUser.setForcePoints(rs.getInt("force_points"));

                String abilitiesJson = rs.getString("unlocked_abilities");
                Map<String, Integer> unlockedAbilities = gson.fromJson(abilitiesJson, new TypeToken<Map<String, Integer>>(){}.getType());
                if (unlockedAbilities != null) {
                    forceUser.getUnlockedAbilities().putAll(unlockedAbilities);
                }

                // Load passives
                String passivesJson = rs.getString("unlocked_passives");
                if (passivesJson != null) {
                    Map<String, Integer> unlockedPassives = gson.fromJson(passivesJson, new TypeToken<Map<String, Integer>>(){}.getType());
                    if (unlockedPassives != null) {
                        forceUser.getUnlockedPassives().putAll(unlockedPassives);
                    }
                }

                String slotBindsJson = rs.getString("slot_binds");
                if (slotBindsJson != null) {
                    Map<Integer, String> slotBinds = gson.fromJson(slotBindsJson, new TypeToken<Map<Integer, String>>(){}.getType());
                    if (slotBinds != null) {
                        forceUser.getSlotBinds().putAll(slotBinds);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load player data for " + uuid, e);
        }
        return forceUser;
    }

    public synchronized void savePlayerData(ForceUser forceUser) {
        connect();
        String sql = "INSERT OR REPLACE INTO force_users (uuid, side, active_ability, force_level, force_xp, force_points, unlocked_abilities, unlocked_passives, slot_binds) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, forceUser.getUuid().toString());
            pstmt.setString(2, forceUser.getSide().name());
            pstmt.setNull(3, java.sql.Types.VARCHAR);
            pstmt.setInt(4, forceUser.getForceLevel());
            pstmt.setDouble(5, forceUser.getForceXp());
            pstmt.setInt(6, forceUser.getForcePoints());
            pstmt.setString(7, gson.toJson(forceUser.getUnlockedAbilities()));
            pstmt.setString(8, gson.toJson(forceUser.getUnlockedPassives()));
            pstmt.setString(9, gson.toJson(forceUser.getSlotBinds()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player data for " + forceUser.getUuid(), e);
        }
    }
}