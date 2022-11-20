package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EcoBalCmd extends EcoBaseCmd {

    public EcoBalCmd() {
        super("balance", "bal", true);
        this.usage = "balance";
        registerPermission("balance.other", false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean run() {
        OfflinePlayer offlinePlayer;
        if (args.length == 1) {
            if (!sender.hasPermission("eco.command.balance.other")) {
                Message.CMD_BAL_NO_PERM_OTHER.sendMessage(sender);
                return true;
            }
            offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        } else if (sender instanceof Player) {
            offlinePlayer = ((Player) sender);
        } else {
            Message.CMD_BAL_NO_CONSOLE.sendMessage(sender);

            return true;
        }
        EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
        if (economyPlayer != null) {
            double balance = economyPlayer.getBalance();
            if (offlinePlayer == sender) {
                Message.CMD_BAL_BALANCE.replaceMoney(balance).sendMessage(sender);
            } else {
                Message.CMD_BAL_BALANCE_OTHER
                        .replacePlayer(offlinePlayer)
                        .replaceMoney(balance)
                        .sendMessage(sender);
            }
        } else {
            Message.NO_ACCOUNT.replacePlayer(offlinePlayer).sendMessage(sender);
        }
        return true;
    }

}
