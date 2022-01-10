package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoSetCmd extends EcoBaseCmd {

    public EcoSetCmd() {
        super("set", null, false);
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
                    Message.CMD_SET_SUCCESS
                            .replacePlayer(offlinePlayer)
                            .replaceMoney(amount)
                            .sendMessage(sender);
                } else {
                    Message.NO_ACCOUNT.replacePlayer(offlinePlayer).sendMessage(sender);
                }
                return true;
            }
        }
        return false;
    }

}
