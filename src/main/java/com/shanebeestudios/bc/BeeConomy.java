package com.shanebeestudios.bc;

import com.shanebeestudios.bc.command.EcoAddCmd;
import com.shanebeestudios.bc.command.EcoBalCmd;
import com.shanebeestudios.bc.command.EcoBaseCmd;
import com.shanebeestudios.bc.command.EcoPayCmd;
import com.shanebeestudios.bc.command.EcoRemoveCmd;
import com.shanebeestudios.bc.command.EcoSetCmd;
import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.config.PlayerConfig;
import com.shanebeestudios.bc.eco.CustomEconomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.listener.CommandListener;
import com.shanebeestudios.bc.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BeeConomy extends JavaPlugin {

    private static BeeConomy instance;
    private CommandListener commandListener;
    private Config config;
    private EconomyManager ecoManager;
    private PlayerConfig playerConfig;

    @Override
    public void onEnable() {
        instance = this;

        this.config = new Config(this);
        this.ecoManager = new EconomyManager(this);
        this.playerConfig = new PlayerConfig(this);

        if (registerEconomy()) {
            Util.log("Vault hook is &asuccessful");
        } else {
            Util.error("Could not hook into vault, plugin disabling");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        registerCommands();

        Util.log("BeeConomy successfully &aloaded!");
    }

    @Override
    public void onDisable() {
        this.ecoManager.saveAllPlayers();
        this.ecoManager = null;
        this.config = null;
        this.playerConfig = null;
        this.commandListener = null;
        instance = null;
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        Map<String, EcoBaseCmd> commands = new HashMap<>();
        commands.put("set", new EcoSetCmd());
        commands.put("add", new EcoAddCmd());
        commands.put("remove", new EcoRemoveCmd());
        commands.put("pay", new EcoPayCmd());
        commands.put("balance", new EcoBalCmd());

        this.commandListener = new CommandListener(commands);
        PluginCommand command = getCommand("eco");
        command.setExecutor(commandListener);
    }

    private boolean registerEconomy() {
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault != null && vault.isEnabled()) {
            Bukkit.getServicesManager().register(Economy.class, new CustomEconomy(this), vault, ServicePriority.Normal);
            return true;
        }
        return false;
    }

    @NotNull
    public Config getPluginConfig() {
        return config;
    }

    public EconomyManager getEcoManager() {
        return ecoManager;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    public static BeeConomy getInstance() {
        return instance;
    }

}
