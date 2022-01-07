package com.shanebeestudios.bc.config;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import com.shanebeestudios.bc.util.Util;
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
            if (config.isDouble("players." + key + ".balance")) {
                balance = config.getDouble("players." + key + ".balance");
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(key));
            economyManager.createEconomyPlayerAccount(offlinePlayer, balance);
            amount++;
        }
        Message.PLAYER_CONFIG_LOADED.log(amount, (System.currentTimeMillis() - start));
    }

    public void saveAllPlayers() {
        for (EconomyPlayer ecoPlayer : plugin.getEcoManager().getAllEcoPlayers()) {
            savePlayer(ecoPlayer);
        }
        saveFile();
    }

    public void savePlayer(@NotNull EconomyPlayer economyPlayer) {
        String path = "players." + economyPlayer.getUuid() + ".";
        config.set(path + "balance", economyPlayer.getBalance());
        config.set(path + "name", economyPlayer.getBukkitPlayer().getName());
    }

    public void saveFile() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
