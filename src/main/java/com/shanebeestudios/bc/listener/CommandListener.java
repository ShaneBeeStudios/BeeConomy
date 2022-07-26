package com.shanebeestudios.bc.listener;

import com.google.common.collect.ImmutableList;
import com.shanebeestudios.bc.command.EcoBaseCmd;
import com.shanebeestudios.bc.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandListener implements TabExecutor {

    private final Map<String, EcoBaseCmd> COMMANDS = new HashMap<>();

    public CommandListener(List<Class<? extends EcoBaseCmd>> commands) {
        commands.forEach(cmdClass -> {
            try {
                EcoBaseCmd ecoBaseCmd = cmdClass.newInstance();
                COMMANDS.put(ecoBaseCmd.getName(), ecoBaseCmd);
                String commandAlias = ecoBaseCmd.getAlias();
                if (commandAlias != null) {
                    COMMANDS.put(commandAlias, ecoBaseCmd);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            EcoBaseCmd baseCmd = COMMANDS.get(args[0]);
            if (baseCmd != null) {
                if (sender instanceof Player && !baseCmd.hasPermission(sender)) {
                    Message.NO_PERMISSION.sendMessage(sender);
                    return true;
                }
                baseCmd.processCmd(sender, args);
                return true;
            }
        }
        StringBuilder builder = new StringBuilder();
        List<EcoBaseCmd> availableCommands = new ArrayList<>();
        COMMANDS.values().forEach(command -> {
            if (command.hasPermission(sender) && !availableCommands.contains(command)) {
                availableCommands.add(command);
            }
        });
        availableCommands.forEach(command -> {
            builder.append("&b").append(command.getName());
            if (command.getAlias() != null) {
                builder.append("&7(&b").append(command.getAlias()).append("&7)");
            }
            builder.append("&7, ");
        });

        Message.UNKNOWN_COMMAND.replaceString(builder.toString()).sendMessage(sender);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> commandsWithPerm = getCommandsWithPerm(sender);
            return StringUtil.copyPartialMatches(args[0], commandsWithPerm, new ArrayList<>());
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
                    if (args.length > 2 || !sender.hasPermission("eco.command.balance.other")) {
                        return ImmutableList.of();
                    } else {
                        return null;
                    }
                case "baltop":
                case "balancetop":
                    if (args.length == 2 && sender.hasPermission("eco.command.balancetop.pages")) {
                        return Collections.singletonList("<page>");
                    }
            }
        }
        return ImmutableList.of();
    }

    private List<String> getCommandsWithPerm(CommandSender sender) {
        List<String> commands = new ArrayList<>();
        COMMANDS.forEach((name, baseCmd) -> {
            if (baseCmd.hasPermission(sender)) {
                commands.add(name);
            }
        });
        return commands;
    }

}
