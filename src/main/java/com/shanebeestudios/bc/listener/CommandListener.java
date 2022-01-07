package com.shanebeestudios.bc.listener;

import com.google.common.collect.ImmutableList;
import com.shanebeestudios.bc.command.EcoBaseCmd;
import com.shanebeestudios.bc.util.Message;
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

public class CommandListener implements TabExecutor {

    private final List<EcoBaseCmd> COMMANDS = new ArrayList<>();
    private final List<String> COMMAND_NAMES = new ArrayList<>();
    private final String COMMANDS_STRING;

    public CommandListener(List<Class<? extends EcoBaseCmd>> commands) {
        StringBuilder builder = new StringBuilder();
        commands.forEach(cmdClass -> {
            try {
                EcoBaseCmd ecoBaseCmd = cmdClass.newInstance();
                String cmdName = ecoBaseCmd.getName();
                String alias = ecoBaseCmd.getAlias();
                COMMANDS.add(ecoBaseCmd);
                COMMAND_NAMES.add(cmdName);
                builder.append("&b").append(cmdName);
                if (alias != null) {
                    COMMAND_NAMES.add(alias);
                    builder.append("&7(&b").append(alias).append("&7)");
                }
                builder.append("&7, ");
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        COMMANDS_STRING = builder.substring(0, builder.length() - 2);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            EcoBaseCmd command = null;
            for (EcoBaseCmd baseCmd : COMMANDS) {
                if (baseCmd.matches(args[0])) {
                    command = baseCmd;
                }
            }
            if (command != null) {
                if (sender instanceof Player && !command.hasPermission(sender)) {
                    Message.NO_PERMISSION.sendMessage(sender);
                }
                command.processCmd(sender, args);
                return true;
            }
        }
        Message.UNKNOWN_COMMAND.sendMessage(sender, COMMANDS_STRING);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], COMMAND_NAMES, new ArrayList<>());
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
