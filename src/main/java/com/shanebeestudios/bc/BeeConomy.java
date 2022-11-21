package com.shanebeestudios.bc;

import com.shanebeestudios.bc.command.EcoAddCmd;
import com.shanebeestudios.bc.command.EcoBalCmd;
import com.shanebeestudios.bc.command.EcoBalTopCommand;
import com.shanebeestudios.bc.command.EcoBaseCmd;
import com.shanebeestudios.bc.command.EcoPayCmd;
import com.shanebeestudios.bc.command.EcoRemoveCmd;
import com.shanebeestudios.bc.command.EcoSetCmd;
import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.config.MessageConfig;
import com.shanebeestudios.bc.config.PlayerConfig;
import com.shanebeestudios.bc.eco.CustomEconomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.listener.CommandListener;
import com.shanebeestudios.bc.listener.PlayerListener;
import com.shanebeestudios.bc.util.Message;
import com.shanebeestudios.bc.util.UpdateChecker;
import com.shanebeestudios.bc.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BeeConomy extends JavaPlugin {

    private static BeeConomy instance;
    private CommandListener commandListener;
    private Config config;
    private EconomyManager ecoManager;
    private PlayerConfig playerConfig;
    private MessageConfig messageConfig;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;

        // Load configs
        this.config = new Config(this);
        this.messageConfig = new MessageConfig(this);
        this.ecoManager = new EconomyManager(this);

        // Register economy
        // If not registered, plugin shuts down
        if (!registerEconomy()) return;

        this.playerConfig = new PlayerConfig(this);
        registerCommands();
        registerListeners();
        new Metrics(this, 16794);
        checkUpdate();
        Message.PLUGIN_LOAD_SUCCESS.replaceNumber(System.currentTimeMillis() - start).log();

        // Debug available permissions
        printPermissions();
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
        List<Class<? extends EcoBaseCmd>> commands = new ArrayList<>();
        commands.add(EcoAddCmd.class);
        commands.add(EcoBalCmd.class);
        commands.add(EcoBalTopCommand.class);
        commands.add(EcoPayCmd.class);
        commands.add(EcoRemoveCmd.class);
        commands.add(EcoSetCmd.class);

        this.commandListener = new CommandListener(commands);
        PluginCommand command = getCommand("eco");
        command.setExecutor(commandListener);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new UpdateChecker(this), this);
    }

    private boolean registerEconomy() {
        PluginManager pluginManager = getServer().getPluginManager();
        Plugin vault = pluginManager.getPlugin("Vault");
        if (vault != null && vault.isEnabled()) {
            Bukkit.getServicesManager().register(Economy.class, new CustomEconomy(this), vault, ServicePriority.Normal);
            Message.VAULT_HOOK_SUCCESS.log();
            return true;
        }
        Message.VAULT_HOOK_FAILURE.error();
        pluginManager.disablePlugin(this);
        return false;
    }

    private void printPermissions() {
        if (Config.SETTINGS_DEBUG) {
            Util.log("Registered Permissions:");
            Bukkit.getPluginManager().getPermissions().forEach(permission -> {
                String name = permission.getName();
                if (name.contains("eco.")) {
                    Util.log("- %s : %s", name, permission.getDefault());
                }
            });
        }
    }

    private void checkUpdate() {
        if (Config.SETTINGS_UPDATE_CHECKER) {
            UpdateChecker.checkForUpdate(getDescription().getVersion());
        } else {
            Message.UPDATE_CHECKER_DISABLED.log();
        }
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

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public static BeeConomy getInstance() {
        return instance;
    }

}
