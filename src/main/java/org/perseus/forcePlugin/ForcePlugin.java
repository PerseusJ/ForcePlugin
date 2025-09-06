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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File dataFolder = new File(getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        this.forceUserManager = new ForceUserManager(this);
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.telekinesisManager = new TelekinesisManager(this);
        this.abilityManager = new AbilityManager(this, abilityConfigManager, telekinesisManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.guiManager = new GUIManager(abilityManager, forceUserManager);

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(forceUserManager, forceBarManager), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(forceUserManager, abilityManager, cooldownManager, forceBarManager, telekinesisManager), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ActionBarListener(forceUserManager, abilityManager), this);

        getCommand("force").setExecutor(new ForceCommand(forceUserManager));
        getCommand("powers").setExecutor(new PowersCommand(forceUserManager));
        getCommand("abilities").setExecutor(new AbilitiesCommand(this));
        getCommand("forceadmin").setExecutor(new ForceAdminCommand(this));

        for (Player player : getServer().getOnlinePlayers()) {
            forceUserManager.loadPlayerData(player);
            forceBarManager.addPlayer(player);
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

    // --- FIX: The @Override annotation has been removed from this custom method ---
    public void reloadPluginConfig() {
        reloadConfig();
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.abilityManager.reload(this, this.abilityConfigManager, this.telekinesisManager);
        this.forceBarManager.reloadConfig();
    }

    public ForceUserManager getForceUserManager() { return forceUserManager; }
    public AbilityManager getAbilityManager() { return abilityManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public ForceBarManager getForceBarManager() { return forceBarManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public AbilityConfigManager getAbilityConfigManager() { return abilityConfigManager; }
    public TelekinesisManager getTelekinesisManager() { return telekinesisManager; }
}