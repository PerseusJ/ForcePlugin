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
        this.abilityManager = new AbilityManager(this, abilityConfigManager, telekinesisManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.guiManager = new GUIManager(abilityManager, forceUserManager, abilityConfigManager);
        this.ambientEffectsManager = new AmbientEffectsManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(forceUserManager, forceBarManager, this), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(forceUserManager, abilityManager, cooldownManager, forceBarManager, telekinesisManager, levelingManager), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ActionBarListener(forceUserManager, abilityManager), this);
        getServer().getPluginManager().registerEvents(new ProjectileDeflectionListener(), this);
        getServer().getPluginManager().registerEvents(new ExperienceListener(levelingManager), this);

        // Register commands
        getCommand("force").setExecutor(new ForceCommand(forceUserManager));
        getCommand("powers").setExecutor(new PowersCommand(forceUserManager));
        getCommand("abilities").setExecutor(new AbilitiesCommand(this));
        getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));
        getCommand("forcestats").setExecutor(new ForceStatsCommand(this));

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
}