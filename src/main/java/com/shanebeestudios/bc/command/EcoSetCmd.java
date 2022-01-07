package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Util;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoSetCmd extends EcoBaseCmd {

    public EcoSetCmd() {
        super("set", null);
        this.usage = "set <player> <amount>";
    }

    @Override
    public boolean run() {
        if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
                if (economyPlayer != null) {
                    economyPlayer.setBalance(amount);
                    Util.sendColMsg(sender, "&7Balance of &b%s &7was set to &b%s%.2f",
                            offlinePlayer.getName(), Config.ECO_SYMBOL, amount);
                } else {
                    Util.sendColMsg(sender, "&b%s&c does not have an account.", offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }

}
