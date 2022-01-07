package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.config.Config;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EcoBalCmd extends EcoBaseCmd {

    public EcoBalCmd() {
        super("balance", "bal");
        this.usage = "balance";
    }

    @Override
    public boolean run() {
        OfflinePlayer offlinePlayer;
        if (args.length == 1) {
            offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        } else if (sender instanceof Player) {
            offlinePlayer = ((Player) sender);
        } else {
            Util.sendColMsg(sender, "&cSilly console you don't have any %s", Config.ECO_NAME);
            return true;
        }
        EconomyPlayer economyPlayer = economyManager.getEcoPlayer(offlinePlayer);
        if (economyPlayer != null) {
            double balance = economyPlayer.getBalance();
            if (offlinePlayer == sender) {
                Util.sendColMsg(sender, "&7Your balance is &b%s%.2f", Config.ECO_SYMBOL, balance);
            } else {
                Util.sendColMsg(sender, "&7Balance of &b%s &7is &b%s%.2f", offlinePlayer.getName(), Config.ECO_SYMBOL, balance);
            }
        } else {
            Util.sendColMsg(sender, "&b%s&c does not have an account.", offlinePlayer.getName());
        }
        return true;
    }

}
