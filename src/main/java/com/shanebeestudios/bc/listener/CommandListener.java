package com.shanebeestudios.bc.listener;

import com.google.common.collect.ImmutableList;
import com.shanebeestudios.bc.command.EcoBaseCmd;
import com.shanebeestudios.bc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandListener implements TabExecutor {

    private final Map<String, EcoBaseCmd> COMMANDS;
    private final String COMMAND_STRING;

    public CommandListener(Map<String, EcoBaseCmd> commands) {
        this.COMMANDS = commands;
        StringBuilder builder = new StringBuilder();
        for (String s : COMMANDS.keySet()) {
            builder.append("&b").append(s).append("&7, ");
        }
        COMMAND_STRING = builder.toString();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String cmd = args[0];
            if (COMMANDS.containsKey(cmd)) {
                EcoBaseCmd baseCmd = COMMANDS.get(cmd);
                if (sender instanceof Player && !sender.hasPermission("eco.command." + cmd)) {
                    Util.sendColMsg(sender, "&cYou do not have permission to use this command.");
                }
                baseCmd.processCmd(sender, args);
                return true;
            }
        }
        Util.sendColMsg(sender, "&cUnknown command. &7Options: &b" + COMMAND_STRING.substring(0, COMMAND_STRING.length() - 2));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], COMMANDS.keySet(), new ArrayList<>());
        }
        if (args.length >= 2) {
            String arg = args[0];
            switch (arg) {
                case "set":
                case "add":
                case "remove":
                case "pay":
                    if (args.length == 3) {
                        return Collections.singletonList("<amount>");
                    } else if (args.length > 3) {
                        return ImmutableList.of();
                    } else {
                        return null;
                    }
                case "bal":
                case "balance":
                    if (args.length > 2) {
                        return ImmutableList.of();
                    } else {
                        return null;
                    }
            }
        }
        return ImmutableList.of();
    }

}
