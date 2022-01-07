package com.shanebeestudios.bc.util;

import org.bukkit.command.CommandSender;

public class Message {

    // Command Messages General
    public static final Message UNKNOWN_COMMAND = get("&cUnknown command. &7Options: &b%s");
    public static final Message NO_PERMISSION = get("&cYou do not have permission to use this command.");
    public static final Message CMD_CORRECT_USAGE = get("&6Correct Usage: &b/eco %s");
    public static final Message NO_ACCOUNT = get("&b%s&c does not have an account.");

    // Command Messages Commands
    public static final Message CMD_ADD_SUCCESS = get("&b%s%.2f &7has been added to the account of &b%s&7, bring their balance up to &b%s%.2f");
    public static final Message CMD_BAL_NO_PERM_OTHER = get("&cYou do not have permission to check another player's balance!");
    public static final Message CMD_BAL_NO_CONSOLE = get("&cSilly console you don't have any %s");
    public static final Message CMD_BAL_BALANCE = get("&7Your balance is &b%s%.2f");
    public static final Message CMD_BAL_BALANCE_OTHER = get("&7Balance of &b%s &7is &b%s%.2f");
    public static final Message CMD_PAY_NO_CONSOLE = get("&cConsole can not pay players");
    public static final Message CMD_PAY_BELOW_ZERO = get("&cYou can not send an amount less than &b%s0.00");
    public static final Message CMD_PAY_NOT_SELF = get("&cYou can not pay yourself!");
    public static final Message CMD_PAY_NOT_ENOUGH = get("&cYou do not have enough %s to send.");
    public static final Message CMD_PAY_SUCCESS = get("&b%s%.2f &7has been added to the account of &b%s&7, bring their balance up to &b%s%.2f");
    public static final Message CMD_REM_NOT_ENOUGH = get("&b%s &7does not have enough money in their account, they currently have &b%s%.2f");
    public static final Message CMD_REM_SUCCESS = get("&b%s%.2f &7has been remove from the account of &b%s&7, bring their balance down to &b%s%.2f");
    public static final Message CMD_SET_SUCCESS = get("&7Balance of &b%s &7was set to &b%s%.2f");
    public static final Message CMD_BAL_TOP_HEADER = get("&7Top Balances [&6%s&7]");
    public static final Message CMD_BAL_TOP_PAGE = get("&7Page &b%s&7/&b%s");
    public static final Message CMD_BAL_TOP_BALANCE = get("&7[&a%s&7] Player: &b%s&r, &7Balance: &6%s&b%.2f");

    // Config Messages
    public static final Message PLAYER_CONFIG_LOADED = get("%s players have been &aloaded &7in &b%s&7ms!");
    public static final Message PLAYER_CONFIG_NO_PLAYERS = get("No players found!");

    // Logging Messages
    public static final Message VAULT_HOOK_SUCCESS = get("Vault hook is &asuccessful");
    public static final Message VAULT_HOOK_FAILURE = get("Could not hook into vault, plugin disabling");
    public static final Message PLUGIN_LOAD_SUCCESS = get("BeeConomy successfully &aloaded &7in &b%s&7ms");

    private static Message get(String message) {
        return new Message(message);
    }

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public void sendMessage(CommandSender sender, Object... objects) {
        Util.sendColMsg(sender, message, objects);
    }

    public void sendMessageNoPrx(CommandSender sender, Object... objects) {
        Util.sendColMsgNoPre(sender, message, objects);
    }

    public void log(Object... objects) {
        Util.log(message, objects);
    }

}
