package com.shanebeestudios.bc.eco;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.config.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private final BeeConomy plugin;
    private final Map<UUID, EconomyPlayer> ECONOMY_PLAYERS = new HashMap<>();
    private final double START_AMOUNT;

    public EconomyManager(BeeConomy plugin) {
        this.plugin = plugin;
        this.START_AMOUNT = Config.ECO_STARTING_AMOUNT;
        startConfigTimer();
    }

    public boolean hasAccount(OfflinePlayer player) {
        return ECONOMY_PLAYERS.containsKey(player.getUniqueId());
    }

    public EconomyPlayer createEconomyPlayerAccount(@NotNull OfflinePlayer player, double balance) {
        EconomyPlayer economyPlayer = new EconomyPlayer(player.getUniqueId(), balance);
        ECONOMY_PLAYERS.put(player.getUniqueId(), economyPlayer);
        return economyPlayer;
    }

    public EconomyPlayer createEconomyPlayerAccount(OfflinePlayer player) {
        return createEconomyPlayerAccount(player, START_AMOUNT);
    }

    /**
     * Will return an {@link EconomyPlayer} if an account is created,
     * or will create a new account.
     *
     * @param player Player to check for account
     * @return account if already created, or new account.
     */
    @Nullable
    public EconomyPlayer getEcoPlayer(OfflinePlayer player) {
        if (hasAccount(player)) {
            return ECONOMY_PLAYERS.get(player.getUniqueId());
        }
        return createEconomyPlayerAccount(player);
    }

    public Collection<EconomyPlayer> getAllEcoPlayers() {
        return ECONOMY_PLAYERS.values();
    }

    public void saveAllPlayers() {
        PlayerConfig playerConfig = plugin.getPlayerConfig();
        if (playerConfig != null) {
            playerConfig.saveAllPlayers();
        }
    }

    private void startConfigTimer() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllPlayers, 6000, 6000);
    }

}
