package org.perseus.forcePlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File dataFolder = new File(getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // Initialize managers
        this.forceUserManager = new ForceUserManager(this);
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.telekinesisManager = new TelekinesisManager(this);
        this.levelingManager = new LevelingManager(this);
        this.holocronManager = new HolocronManager(this);
        this.abilityManager = new AbilityManager(this, abilityConfigManager, telekinesisManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.guiManager = new GUIManager(abilityManager, forceUserManager, abilityConfigManager);
        this.ambientEffectsManager = new AmbientEffectsManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(forceUserManager, forceBarManager, this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileDeflectionListener(), this);
        getServer().getPluginManager().registerEvents(new ExperienceListener(levelingManager), this);
        getServer().getPluginManager().registerEvents(new HolocronListener(this), this);

        // Register commands
        getCommand("force").setExecutor(new ForceCommand(forceUserManager, holocronManager));
        getCommand("forcestats").setExecutor(new ForceStatsCommand(this));
        getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));

        // --- NEW: Hook into PlaceholderAPI ---
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ForcePlaceholders(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        } else {
            getLogger().warning("PlaceholderAPI not found. Placeholders will not work.");
        }
        // --- END NEW ---

        // Handle online players on startup/reload
        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.loadPlayerData(player);
            forceBarManager.addPlayer(player);
            levelingManager.updateXpBar(player);
        }
        getLogger().info("ForcePlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.savePlayerData(player);
        }
        getLogger().info("ForcePlugin has been disabled!");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.abilityManager.reload(this, this.abilityConfigManager, this.telekinesisManager);
        this.forceBarManager.reloadConfig();
        this.levelingManager.loadConfigValues();
        this.guiManager = new GUIManager(this.abilityManager, this.forceUserManager, this.abilityConfigManager);
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
}