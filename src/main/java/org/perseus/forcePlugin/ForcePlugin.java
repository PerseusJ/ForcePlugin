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

public class ForcePlugin extends JavaPlugin {

    private ForceUserManager forceUserManager;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;
    private ForceBarManager forceBarManager;
    private GUIManager guiManager;
    private AbilityConfigManager abilityConfigManager;
    private TelekinesisManager telekinesisManager;
    private LevelingManager levelingManager;
    private DatabaseManager databaseManager;
    private RankManager rankManager;
    private VersionAdapter versionAdapter;
    private PassiveManager passiveManager;
    private ForceEnchantManager forceEnchantManager;
    private HudManager hudManager;
    private int autoSaveTaskId = -1;

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

        // Initialize database with retry logic
        this.databaseManager = new DatabaseManager(this);
        if (!this.databaseManager.connect(3)) {
            getLogger().severe("!!! Could not connect to the database after 3 attempts. Disabling plugin. !!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.forceUserManager = new ForceUserManager(this, databaseManager);
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.telekinesisManager = new TelekinesisManager(this);
        this.levelingManager = new LevelingManager(this);
        this.abilityManager = new AbilityManager(this, abilityConfigManager, telekinesisManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.rankManager = new RankManager(this);
        this.passiveManager = new PassiveManager(this);
        this.forceEnchantManager = new ForceEnchantManager(this);
        this.guiManager = new GUIManager(this, abilityManager, forceUserManager, abilityConfigManager, rankManager, passiveManager);
        new AmbientEffectsManager(this);
        this.hudManager = new HudManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileDeflectionListener(), this);
        getServer().getPluginManager().registerEvents(new ExperienceListener(this), this);
        getServer().getPluginManager().registerEvents(new UltimateAbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new PassiveListener(this), this);
        getServer().getPluginManager().registerEvents(new HotbarListener(this), this);
        getServer().getPluginManager().registerEvents(hudManager, this);

        // Register commands (null-checked to avoid NPEs if plugin.yml is misconfigured)
        if (getCommand("force") != null) {
            org.perseus.forcePlugin.commands.ForceTabCompleter forceTabCompleter = new org.perseus.forcePlugin.commands.ForceTabCompleter();
            forceTabCompleter.setPlugin(this);
            getCommand("force").setExecutor(new ForceCommand(this));
            getCommand("force").setTabCompleter(forceTabCompleter);
        }
        if (getCommand("forcestats") != null) {
            getCommand("forcestats").setExecutor(new ForceStatsCommand(this));
            getCommand("forcestats").setTabCompleter(new org.perseus.forcePlugin.commands.ForceTabCompleter());
        }
        if (getCommand("forceadmin") != null) {
            getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));
            getCommand("forceadmin").setTabCompleter(new org.perseus.forcePlugin.commands.ForceTabCompleter());
        }
        if (getCommand("forceenchant") != null) {
            getCommand("forceenchant").setExecutor(new ForceEnchantCommand(this));
            getCommand("forceenchant").setTabCompleter(new org.perseus.forcePlugin.commands.ForceTabCompleter());
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ForcePlaceholders(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        }

        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.loadPlayerData(player);
        }

        // Start auto-save interval
        startAutoSaveTask();

        getLogger().info("ForcePlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Cancel auto-save task
        if (autoSaveTaskId != -1) {
            getServer().getScheduler().cancelTask(autoSaveTaskId);
            autoSaveTaskId = -1;
        }

        if (hudManager != null) {
            hudManager.removeAll();
        }
        if (forceUserManager != null) {
            forceUserManager.saveAllOnlinePlayers();
            // Wait for pending async saves to complete (up to 10 seconds)
            int waited = 0;
            while (forceUserManager.getPendingSaveCount() > 0 && waited < 100) {
                try {
                    Thread.sleep(100);
                    waited++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            if (forceUserManager.getPendingSaveCount() > 0) {
                getLogger().warning("Timed out waiting for " + forceUserManager.getPendingSaveCount() + " pending saves to complete.");
            }
        }
        if (this.databaseManager != null) {
            this.databaseManager.disconnect();
        }
        getLogger().info("ForcePlugin has been disabled!");
    }

    private void startAutoSaveTask() {
        int intervalMinutes = getConfig().getInt("database.auto-save-interval-minutes", 5);
        long intervalTicks = intervalMinutes * 60L * 20L;
        if (intervalTicks <= 0) {
            intervalTicks = 5 * 60 * 20; // default 5 minutes
        }
        autoSaveTaskId = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (forceUserManager != null) {
                forceUserManager.saveAllOnlinePlayers();
                getLogger().info("Auto-save completed for all online players.");
            }
        }, intervalTicks, intervalTicks).getTaskId();
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
        this.hudManager.reloadConfig();
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
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public RankManager getRankManager() { return rankManager; }
    public VersionAdapter getVersionAdapter() { return versionAdapter; }
    public PassiveManager getPassiveManager() { return passiveManager; }
    public ForceEnchantManager getForceEnchantManager() { return forceEnchantManager; }
    public HudManager getHudManager() { return hudManager; }
}