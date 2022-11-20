package com.shanebeestudios.bc.eco;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.config.Config;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

@SuppressWarnings("deprecation")
public class CustomEconomy extends AbstractEconomy {

    private final BeeConomy plugin;
    private final EconomyManager economyManager;
    private final String CURRENCY_NAME;
    private final String CURRENCY_SYMBOL;

    public CustomEconomy(BeeConomy plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEcoManager();
        CURRENCY_NAME = Config.ECO_NAME;
        CURRENCY_SYMBOL = Config.ECO_SYMBOL;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%s%.2f", CURRENCY_SYMBOL, amount);
    }

    @Override
    public String currencyNamePlural() {
        return CURRENCY_NAME;
    }

    @Override
    public String currencyNameSingular() {
        return CURRENCY_NAME;
    }

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return economyManager.hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return getBalance(offlinePlayer);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        EconomyPlayer ecoPlayer = economyManager.getEcoPlayer(offlinePlayer);
        if (ecoPlayer == null) {
            return 0;
        }
        return ecoPlayer.getBalance();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) > amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return getBalance(offlinePlayer) > amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        double balance = getBalance(offlinePlayer);
        if (balance >= amount) {
            EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
            if (economyPlayer == null) {
                return new EconomyResponse(amount, 0, ResponseType.FAILURE, "no account");
            }
            economyPlayer.withdraw(amount);
            return new EconomyResponse(amount, economyPlayer.getBalance(), ResponseType.SUCCESS, "has money");
        } else {
            return new EconomyResponse(amount, balance, ResponseType.FAILURE, "not enough money");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
        if (economyPlayer == null) {
            return new EconomyResponse(amount, 0, ResponseType.FAILURE, "no account");
        }
        economyPlayer.deposit(amount);
        return new EconomyResponse(amount, economyPlayer.getBalance(), ResponseType.SUCCESS, "money deposited");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return createPlayerAccount(offlinePlayer);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        EconomyPlayer economyPlayer = economyManager.createEconomyPlayerAccount(offlinePlayer);
        return economyPlayer != null;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }
}
