package com.shanebeestudios.bc.config;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerConfig {

    private final BeeConomy plugin;
    private final EconomyManager economyManager;
    private File configFile;
    private FileConfiguration config;

    public PlayerConfig(BeeConomy plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEcoManager();
        loadAllPlayers();
    }

    public void loadAllPlayers() {
        long start = System.currentTimeMillis();
        this.configFile = new File(plugin.getDataFolder(), "balances.yml");
        if (!configFile.exists()) {
            plugin.saveResource("balances.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection section = config.getConfigurationSection("players");
        if (section == null) {
            Message.PLAYER_CONFIG_NO_PLAYERS.log();
            return;
        }
        int amount = 0;
        for (String key : section.getKeys(false)) {
            double balance = 0.0;
            String name = null;
            if (config.isDouble("players." + key + ".balance")) {
                balance = config.getDouble("players." + key + ".balance");
            }
            if (config.isString("players." + key + ".name")) {
                name = config.getString("players." + key + ".name");
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(key));
            economyManager.createEconomyPlayerAccount(offlinePlayer, balance, name);
            amount++;
        }
        Message.PLAYER_CONFIG_LOADED
                .replaceNumber(amount)
                .replaceNumber((System.currentTimeMillis() - start))
                .log();
    }

    public void saveAllPlayers(boolean log) {
        if (log) {
            Message.PLAYER_CONFIG_SAVING.log();
        }
        long start = System.currentTimeMillis();
        int flagged = 0;
        for (EconomyPlayer ecoPlayer : plugin.getEcoManager().getFlaggedForSaving()) {
            savePlayer(ecoPlayer);
            flagged++;
        }
        if (flagged > 0) {
            // Only save file if something actually changed
            saveFile();
        }
        long finish = System.currentTimeMillis() - start;
        if (log) {
            Message.PLAYER_CONFIG_SAVED.replaceNumber(flagged).replaceNumber(finish).log();
        }
    }

    public void savePlayer(@NotNull EconomyPlayer economyPlayer) {
        String path = "players." + economyPlayer.getUuid() + ".";
        config.set(path + "balance", economyPlayer.getBalance());
        config.set(path + "name", economyPlayer.getName());
    }

    public void saveFile() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
