package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import com.shanebeestudios.bc.util.Util;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoRemoveCmd extends EcoBaseCmd {

    public EcoRemoveCmd() {
        super("remove", null);
        this.usage = "remove <player> <amount>";
    }

    @Override
    public boolean run() {
        if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
                if (economyPlayer != null) {
                    if (economyPlayer.getBalance() < amount) {
                        Message.CMD_REM_NOT_ENOUGH.sendMessage(sender, offlinePlayer.getName(), Config.ECO_SYMBOL, economyPlayer.getBalance());
                        return true;
                    }
                    economyPlayer.withdraw(amount);
                    Message.CMD_REM_SUCCESS.sendMessage(sender, Config.ECO_SYMBOL, amount, offlinePlayer.getName(), Config.ECO_SYMBOL, economyPlayer.getBalance());
                } else {
                    Message.NO_ACCOUNT.sendMessage(sender, offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }

}
