package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EcoPayCmd extends EcoBaseCmd {

    public EcoPayCmd() {
        super("pay", null, true);
        this.usage = "pay <player> <amount>";
    }

    @Override
    public boolean run() {
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                Message.CMD_PAY_NO_CONSOLE.sendMessage(sender);
                return true;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    Message.CMD_PAY_BELOW_ZERO.sendMessage(sender, Config.ECO_SYMBOL);
                    return true;
                }
                EconomyPlayer ecoReceiver = economyManager.getEcoPlayer(offlinePlayer);
                EconomyPlayer ecoSender = economyManager.getEcoPlayer(((Player) sender));
                if (ecoReceiver != null) {
                    assert ecoSender != null;
                    if (ecoSender == ecoReceiver) {
                        Message.CMD_PAY_NOT_SELF.sendMessage(sender);
                        return true;
                    }
                    if (ecoSender.getBalance() < amount) {
                        Message.CMD_PAY_NOT_ENOUGH.sendMessage(sender, Config.ECO_NAME);
                        return true;
                    }
                    ecoReceiver.deposit(amount);
                    ecoSender.withdraw(amount);
                    Message.CMD_PAY_SUCCESS.sendMessage(sender, Config.ECO_SYMBOL, amount, offlinePlayer.getName(), Config.ECO_SYMBOL, ecoReceiver.getBalance());
                } else {
                    Message.NO_ACCOUNT.sendMessage(sender, offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }
}
