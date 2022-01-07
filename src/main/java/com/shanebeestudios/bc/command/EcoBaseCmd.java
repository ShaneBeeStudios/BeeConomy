package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.util.Util;
import org.bukkit.command.CommandSender;

public abstract class EcoBaseCmd {

    final BeeConomy plugin;
    final EconomyManager economyManager;
    CommandSender sender;
    String cmdName;
    String cmdAlias;
    String[] args;
    String usage = "";

    public EcoBaseCmd(String cmdName, String cmdAlias) {
        this.cmdName = cmdName;
        this.cmdAlias = cmdAlias;
        this.plugin = BeeConomy.getInstance();
        this.economyManager = plugin.getEcoManager();
    }

    public void processCmd(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = new String[args.length - 1];

        for (int i = 1; i < args.length; i++) { // TODO make sure these numbers work before shrinking this per suggestion
            this.args[i - 1] = args[i];
        }

        if (!run()) {
            Util.sendColMsg(sender, "&6Correct Usage: &b/eco " + usage);
        }
    }

    public abstract boolean run();

    public String getName() {
        return cmdName;
    }

    public String getAlias() {
        return cmdAlias;
    }

    public boolean matches(String command) {
        return cmdName.equalsIgnoreCase(command) || (cmdAlias != null && cmdAlias.equalsIgnoreCase(command));
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("eco.command." + cmdName);
    }

}
