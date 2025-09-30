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

    public synchronized void connect() {
        File dataFolder = new File(plugin.getDataFolder(), "playerdata.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create database file!", e);
            }
        }

        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            plugin.getLogger().info("Successfully connected to the SQLite database.");
            initializeDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to the SQLite database!", e);
        }
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
                + "specialization TEXT,"
                + "needs_choice INTEGER NOT NULL DEFAULT 0"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            addColumnIfNotExists("specialization", "TEXT");
            addColumnIfNotExists("needs_choice", "INTEGER NOT NULL DEFAULT 0");
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
                forceUser.setActiveAbilityId(rs.getString("active_ability"));
                forceUser.setForceLevel(rs.getInt("force_level"));
                forceUser.setForceXp(rs.getDouble("force_xp"));
                forceUser.setForcePoints(rs.getInt("force_points"));

                String json = rs.getString("unlocked_abilities");
                Map<String, Integer> unlocked = gson.fromJson(json, new TypeToken<Map<String, Integer>>(){}.getType());
                if (unlocked != null) {
                    forceUser.getUnlockedAbilities().putAll(unlocked);
                }

                forceUser.setSpecialization(rs.getString("specialization"));
                forceUser.setNeedsToChoosePath(rs.getInt("needs_choice") == 1);
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load player data for " + uuid, e);
        }
        return forceUser;
    }

    public synchronized void savePlayerData(ForceUser forceUser) {
        connect();
        String sql = "INSERT OR REPLACE INTO force_users (uuid, side, active_ability, force_level, force_xp, force_points, unlocked_abilities, specialization, needs_choice) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, forceUser.getUuid().toString());
            pstmt.setString(2, forceUser.getSide().name());
            pstmt.setString(3, forceUser.getActiveAbilityId());
            pstmt.setInt(4, forceUser.getForceLevel());
            pstmt.setDouble(5, forceUser.getForceXp());
            pstmt.setInt(6, forceUser.getForcePoints());
            pstmt.setString(7, gson.toJson(forceUser.getUnlockedAbilities()));
            pstmt.setString(8, forceUser.getSpecialization());
            pstmt.setInt(9, forceUser.needsToChoosePath() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player data for " + forceUser.getUuid(), e);
        }
    }
}