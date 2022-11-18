package com.shanebeestudios.bc.eco;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EconomyPlayer {

    private final UUID uuid;
    private double balance;
    private String name;
    private final EconomyManager economyManager;

    EconomyPlayer(UUID uuid, double balance, @Nullable String name, EconomyManager economyManager) {
        this.uuid = uuid;
        this.balance = balance;
        this.name = name;
        this.economyManager = economyManager;
    }

    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public OfflinePlayer getBukkitPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public String getName() {
        if (name == null) {
            name = getBukkitPlayer().getName();
        }
        return name;
    }

    /**
     * Get the balance of a player
     *
     * @return Balance of player
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the balance of a player
     *
     * @param balance Amount to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
        this.economyManager.flagForSaving(this);
    }

    /**
     * Add to the balance of a player
     *
     * @param amount Amount to add
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.economyManager.flagForSaving(this);
    }

    /**
     * Remove from the balance of a player
     *
     * @param amount Amount to remove
     */
    public void withdraw(double amount) {
        this.balance -= amount;
        this.economyManager.flagForSaving(this);
    }

}
