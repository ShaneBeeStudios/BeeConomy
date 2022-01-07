package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
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
                        Util.sendColMsg(sender, "&b%s &7does not have enough money in their account, they currently have &b%s%.2f",
                                offlinePlayer.getName(), Config.ECO_SYMBOL, economyPlayer.getBalance());
                        return true;
                    }
                    economyPlayer.withdraw(amount);
                    Util.sendColMsg(sender, "&b%s%.2f &7has been remove from the account of &b%s&7, bring their balance down to &b%s%.2f",
                            Config.ECO_SYMBOL, amount, offlinePlayer.getName(), Config.ECO_SYMBOL, economyPlayer.getBalance());
                } else {
                    Util.sendColMsg(sender, "&b%s&c does not have an account.", offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }

}
