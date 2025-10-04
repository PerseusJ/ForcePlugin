package org.perseus.forcePlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.perseus.forcePlugin.commands.ForceAdminCommand;
import org.perseus.forcePlugin.commands.ForceCommand;
import org.perseus.forcePlugin.commands.ForceEnchantCommand;
import org.perseus.forcePlugin.commands.ForceStatsCommand;
import org.perseus.forcePlugin.data.DatabaseManager;
import org.perseus.forcePlugin.gui.GUIManager;
import org.perseus.forcePlugin.gui.GUIListener;
import org.perseus.forcePlugin.listeners.*;
import org.perseus.forcePlugin.managers.*;
import org.perseus.forcePlugin.versioning.Adapter_1_16;
import org.perseus.forcePlugin.versioning.Adapter_1_21;
import org.perseus.forcePlugin.versioning.VersionAdapter;

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
    private VersionAdapter versionAdapter;
    private PassiveManager passiveManager;
    private ForceEnchantManager forceEnchantManager;

    @Override
    public void onEnable() {
        if (!setupVersionAdapter()) {
            getLogger().severe("!!! This version of Minecraft is not supported. Disabling plugin. !!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        // Use 'false' to only create if they don't exist
        saveResource("ranks.yml", false);
        saveResource("passives.yml", false);

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
        this.passiveManager = new PassiveManager(this);
        this.forceEnchantManager = new ForceEnchantManager(this);
        this.guiManager = new GUIManager(this, abilityManager, forceUserManager, abilityConfigManager, rankManager, passiveManager);
        this.ambientEffectsManager = new AmbientEffectsManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileDeflectionListener(), this);
        getServer().getPluginManager().registerEvents(new ExperienceListener(levelingManager), this);
        getServer().getPluginManager().registerEvents(new HolocronListener(this), this);
        getServer().getPluginManager().registerEvents(new UltimateAbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new PassiveListener(this), this);

        // Register commands
        getCommand("force").setExecutor(new ForceCommand(this));
        getCommand("forcestats").setExecutor(new ForceStatsCommand(this));
        getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));
        getCommand("forceenchant").setExecutor(new ForceEnchantCommand(this));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ForcePlaceholders(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        }

        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.loadPlayerData(player);
        }
        getLogger().info("ForcePlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            if (forceUserManager != null && forceUserManager.getForceUser(player) != null) {
                forceUserManager.savePlayerData(player);
            }
        }
        if (this.databaseManager != null) {
            this.databaseManager.disconnect();
        }
        getLogger().info("ForcePlugin has been disabled!");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        // --- THE FIX: Changed 'true' to 'false' to prevent overwriting ---
        saveResource("ranks.yml", false);
        saveResource("passives.yml", false);
        // --- END FIX ---

        this.abilityConfigManager = new AbilityConfigManager(this);
        this.abilityManager.reload(this, this.abilityConfigManager, this.telekinesisManager);
        this.forceBarManager.reloadConfig();
        this.levelingManager.loadConfigValues();
        this.rankManager.loadRanks();
        this.passiveManager.loadPassives();
        this.forceEnchantManager.loadEnchantmentData();
        this.guiManager = new GUIManager(this, this.abilityManager, this.forceUserManager, this.abilityConfigManager, this.rankManager, this.passiveManager);
        getLogger().info("ForcePlugin configuration has been reloaded.");
    }

    private boolean setupVersionAdapter() {
        String version = Bukkit.getServer().getBukkitVersion();
        getLogger().info("Detected server version: " + version);

        if (version.contains("1.17") || version.contains("1.18") || version.contains("1.19") || version.contains("1.20") || version.contains("1.21")) {
            this.versionAdapter = new Adapter_1_21();
            return true;
        } else if (version.contains("1.16")) {
            this.versionAdapter = new Adapter_1_16();
            return true;
        } else {
            return false;
        }
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
    public VersionAdapter getVersionAdapter() { return versionAdapter; }
    public PassiveManager getPassiveManager() { return passiveManager; }
    public ForceEnchantManager getForceEnchantManager() { return forceEnchantManager; }
}