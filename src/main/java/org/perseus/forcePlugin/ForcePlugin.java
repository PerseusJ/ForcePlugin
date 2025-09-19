package org.perseus.forcePlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.perseus.forcePlugin.commands.ForceAdminCommand;
import org.perseus.forcePlugin.commands.ForceCommand;
import org.perseus.forcePlugin.commands.ForceStatsCommand;
import org.perseus.forcePlugin.data.DatabaseManager;
import org.perseus.forcePlugin.gui.GUIManager;
import org.perseus.forcePlugin.gui.GUIListener;
import org.perseus.forcePlugin.listeners.AbilityListener;
import org.perseus.forcePlugin.listeners.ExperienceListener;
import org.perseus.forcePlugin.listeners.HolocronListener;
import org.perseus.forcePlugin.listeners.PlayerConnectionListener;
import org.perseus.forcePlugin.listeners.ProjectileDeflectionListener;
import org.perseus.forcePlugin.managers.*;

import java.io.File;

public class ForcePlugin extends JavaPlugin {

    private ForceUserManager forceUserManager;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;
    private ForceBarManager forceBarManager;
    private GUIManager guiManager;
    private AbilityConfigManager abilityConfigManager;
    private TelekinesisManager telekinesisManager;
    private LevelingManager levelingManager;
    private AmbientEffectsManager ambientEffectsManager;
    private HolocronManager holocronManager;
    private DatabaseManager databaseManager;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("ranks.yml", false);

        // Initialize managers
        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.connect();
        this.forceUserManager = new ForceUserManager(this, databaseManager);
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.telekinesisManager = new TelekinesisManager(this);
        this.levelingManager = new LevelingManager(this);
        this.holocronManager = new HolocronManager(this);
        this.abilityManager = new AbilityManager(this, abilityConfigManager, telekinesisManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.rankManager = new RankManager(this);
        // --- THE FIX: Pass the RankManager to the GUIManager constructor ---
        this.guiManager = new GUIManager(abilityManager, forceUserManager, abilityConfigManager, rankManager);
        this.ambientEffectsManager = new AmbientEffectsManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileDeflectionListener(), this);
        getServer().getPluginManager().registerEvents(new ExperienceListener(levelingManager), this);
        getServer().getPluginManager().registerEvents(new HolocronListener(this), this);

        // Register commands
        getCommand("force").setExecutor(new ForceCommand(this));
        getCommand("forcestats").setExecutor(new ForceStatsCommand(this));
        getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));

        // Handle online players on startup/reload
        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.loadPlayerData(player);
        }
        getLogger().info("ForcePlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (forceUserManager.getForceUser(player) != null) {
                forceUserManager.savePlayerData(player);
            }
        }
        this.databaseManager.disconnect();
        getLogger().info("ForcePlugin has been disabled!");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        saveResource("ranks.yml", true); // Overwrite ranks.yml on reload to ensure it's up to date
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.abilityManager.reload(this, this.abilityConfigManager, this.telekinesisManager);
        this.forceBarManager.reloadConfig();
        this.levelingManager.loadConfigValues();
        this.rankManager.loadRanks();
        // --- THE FIX: Pass the RankManager to the GUIManager constructor during reload ---
        this.guiManager = new GUIManager(this.abilityManager, this.forceUserManager, this.abilityConfigManager, this.rankManager);
    }

    // --- Getters for Managers ---
    public ForceUserManager getForceUserManager() { return forceUserManager; }
    public AbilityManager getAbilityManager() { return abilityManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public ForceBarManager getForceBarManager() { return forceBarManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public AbilityConfigManager getAbilityConfigManager() { return abilityConfigManager; }
    public TelekinesisManager getTelekinesisManager() { return telekinesisManager; }
    public LevelingManager getLevelingManager() { return levelingManager; }
    public HolocronManager getHolocronManager() { return holocronManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public RankManager getRankManager() { return rankManager; }
}