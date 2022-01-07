package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoAddCmd extends EcoBaseCmd {

    public EcoAddCmd() {
        super("add", null);
        this.usage = "add <player> <amount>";
    }

    @Override
    public boolean run() {
        if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
                if (economyPlayer != null) {
                    economyPlayer.deposit(amount);
                    Message.CMD_ADD_SUCCESS.sendMessage(sender, Config.ECO_SYMBOL, amount, offlinePlayer.getName(), Config.ECO_SYMBOL, economyPlayer.getBalance());
                } else {
                    Message.NO_ACCOUNT.sendMessage(sender, offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }

}
