package com.shanebeestudios.bc.command;

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

    @SuppressWarnings("deprecation")
    @Override
    public boolean run() {
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                Message.CMD_PAY_NO_CONSOLE.sendMessage(sender);
                return true;
            }
            Player sendingPlayer = (Player) sender;
            OfflinePlayer receivingPlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    Message.CMD_PAY_BELOW_ZERO.sendMessage(sender);
                    return true;
                }
                EconomyPlayer ecoReceiver = economyManager.getEcoPlayer(receivingPlayer);
                EconomyPlayer ecoSender = economyManager.getEcoPlayer(sendingPlayer);
                if (ecoReceiver != null) {
                    assert ecoSender != null;
                    if (ecoSender == ecoReceiver) {
                        Message.CMD_PAY_NOT_SELF.sendMessage(sender);
                        return true;
                    }
                    if (ecoSender.getBalance() < amount) {
                        Message.CMD_PAY_NOT_ENOUGH.sendMessage(sender);
                        return true;
                    }
                    ecoReceiver.deposit(amount);
                    ecoSender.withdraw(amount);
                    Message.CMD_PAY_SUCCESS
                            .replaceMoney(amount)
                            .replacePlayer(receivingPlayer)
                            .sendMessage(sender);
                    if (receivingPlayer.isOnline()) {
                        Message.CMD_PAY_RECEIVED
                                .replaceMoney(amount)
                                .replacePlayer(sendingPlayer)
                                .sendMessage(receivingPlayer.getPlayer());
                    }
                } else {
                    Message.NO_ACCOUNT.replacePlayer(receivingPlayer).sendMessage(sender);
                }
                return true;
            }
        }
        return false;
    }
}
