package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EcoRemoveCmd extends EcoBaseCmd {

    public EcoRemoveCmd() {
        super("remove", null, false);
        this.usage = "remove <player> <amount>";
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean run() {
        if (args.length == 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
                if (economyPlayer != null) {
                    if (economyPlayer.getBalance() < amount) {
                        Message.CMD_REM_NOT_ENOUGH
                                .replacePlayer(offlinePlayer)
                                .replaceMoney(economyPlayer.getBalance())
                                .sendMessage(sender);
                        return true;
                    }
                    economyPlayer.withdraw(amount);
                    Message.CMD_REM_SUCCESS
                            .replaceMoney(amount)
                            .replacePlayer(offlinePlayer)
                            .replaceMoney(economyPlayer.getBalance())
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
