package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EcoBalCmd extends EcoBaseCmd {

    public EcoBalCmd() {
        super("balance", "bal", true);
        this.usage = "balance";
        setDefaultPerm("balance.other", false);
    }

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
            Message.CMD_BAL_NO_CONSOLE.sendMessage(sender, Config.ECO_NAME);

            return true;
        }
        EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
        if (economyPlayer != null) {
            double balance = economyPlayer.getBalance();
            if (offlinePlayer == sender) {
                Message.CMD_BAL_BALANCE.sendMessage(sender, Config.ECO_SYMBOL, balance);
            } else {
                Message.CMD_BAL_BALANCE_OTHER.sendMessage(sender, offlinePlayer.getName(), Config.ECO_SYMBOL, balance);
            }
        } else {
            Message.NO_ACCOUNT.sendMessage(sender, offlinePlayer.getName());
        }
        return true;
    }

}
