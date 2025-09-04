package org.perseus.forcePlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ForcePlugin extends JavaPlugin {

    // ... (manager instances are the same)
    private ForceUserManager forceUserManager;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;
    private ForceBarManager forceBarManager;
    private GUIManager guiManager;
    private AbilityConfigManager abilityConfigManager;

    @Override
    public void onEnable() {
        // ... (the onEnable method is exactly the same as before)
        saveDefaultConfig();
        File dataFolder = new File(getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        this.forceUserManager = new ForceUserManager(this);
        this.abilityConfigManager = new AbilityConfigManager(this);
        this.abilityManager = new AbilityManager(this, abilityConfigManager);
        this.cooldownManager = new CooldownManager();
        this.forceBarManager = new ForceBarManager(this, forceUserManager);
        this.guiManager = new GUIManager(abilityManager, forceUserManager);

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(forceUserManager, forceBarManager), this);
        getServer().getPluginManager().registerEvents(new AbilityListener(forceUserManager, abilityManager, cooldownManager, forceBarManager), this);
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

    // --- MODIFIED: The Corrected Reload Method ---
    public void reloadPluginConfig() {
        // 1. Reload the config file from disk.
        reloadConfig();

        // 2. Create a new config manager to hold the new values.
        this.abilityConfigManager = new AbilityConfigManager(this);

        // 3. Tell the EXISTING managers to reload their internal data using the new config.
        this.abilityManager.reload(this, this.abilityConfigManager);
        this.forceBarManager.reloadConfig();
    }
    // --- END MODIFIED ---

    // ... (getters are the same)
    public ForceUserManager getForceUserManager() { return forceUserManager; }
    public AbilityManager getAbilityManager() { return abilityManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public ForceBarManager getForceBarManager() { return forceBarManager; }
    public GUIManager getGuiManager() { return guiManager; }
    public AbilityConfigManager getAbilityConfigManager() { return abilityConfigManager; }
}