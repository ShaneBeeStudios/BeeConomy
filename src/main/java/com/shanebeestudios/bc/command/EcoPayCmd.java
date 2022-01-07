package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Util;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EcoPayCmd extends EcoBaseCmd {

    public EcoPayCmd() {
        super("pay", null);
        this.usage = "pay <player> <amount>";
    }

    @Override
    public boolean run() {
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                Util.sendColMsg(sender, "&cConsole can not pay players");
                return true;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (NumberUtils.isNumber(args[1])) {
                double amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    Util.sendColMsg(sender, "&cYou can not send an amount less than &b%s0.00", Config.ECO_SYMBOL);
                    return true;
                }
                EconomyPlayer ecoReceiver = economyManager.getEcoPlayer(offlinePlayer);
                EconomyPlayer ecoSender = economyManager.getEcoPlayer(((Player) sender));
                if (ecoReceiver != null) {
                    assert ecoSender != null;
                    if (ecoSender == ecoReceiver) {
                        Util.sendColMsg(sender, "&cYou can not pay yourself!");
                        return true;
                    }
                    if (ecoSender.getBalance() < amount) {
                        Util.sendColMsg(sender, "&cYou do not have enough %s to send.", Config.ECO_NAME);
                        return true;
                    }
                    ecoReceiver.deposit(amount);
                    ecoSender.withdraw(amount);
                    Util.sendColMsg(sender, "&b%s%.2f &7has been added to the account of &b%s&7, bring their balance up to &b%s%.2f",
                            Config.ECO_SYMBOL, amount, offlinePlayer.getName(), Config.ECO_SYMBOL, ecoReceiver.getBalance());
                } else {
                    Util.sendColMsg(sender, "&b%s&c does not have an account.", offlinePlayer.getName());
                }
                return true;
            }
        }
        return false;
    }
}
