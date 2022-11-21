package com.shanebeestudios.bc.util;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.config.Config;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class Message {

    // Command Messages General
    public static final Message UNKNOWN_COMMAND = get("unknown-command");
    public static final Message NO_PERMISSION = get("no-permission");
    public static final Message CMD_CORRECT_USAGE = get("cmd-correct-usage");
    public static final Message NO_ACCOUNT = get("no-account");

    // Command Messages Commands
    public static final Message CMD_ADD_SUCCESS = get("cmd-add-success");
    public static final Message CMD_BAL_NO_PERM_OTHER = get("cmd-bal-no-perm-other");
    public static final Message CMD_BAL_NO_CONSOLE = get("cmd-bal-no-console");
    public static final Message CMD_BAL_BALANCE = get("cmd-bal-balance");
    public static final Message CMD_BAL_BALANCE_OTHER = get("cmd-bal-balance-other");
    public static final Message CMD_PAY_NO_CONSOLE = get("cmd-pay-no-console");
    public static final Message CMD_PAY_BELOW_ZERO = get("cmd-pay-below-zero");
    public static final Message CMD_PAY_NOT_SELF = get("cmd-pay-not-self");
    public static final Message CMD_PAY_NOT_ENOUGH = get("cmd-pay-not-enough");
    public static final Message CMD_PAY_SUCCESS = get("cmd-pay-success");
    public static final Message CMD_PAY_RECEIVED = get("cmd-pay-received");
    public static final Message CMD_REM_NOT_ENOUGH = get("cmd-rem-not-enough");
    public static final Message CMD_REM_SUCCESS = get("cmd-rem-success");
    public static final Message CMD_SET_SUCCESS = get("cmd-set-success");
    public static final Message CMD_BAL_TOP_HEADER = get("cmd-bal-top-header");
    public static final Message CMD_BAL_TOP_PAGE = get("cmd-bal-top-page");
    public static final Message CMD_BAL_TOP_BALANCE = get("cmd-bal-top-balance");
    public static final Message CMD_BAL_TOP_BALANCE_SORTING = get("cmd-bal-top-balance-sorting");

    // Config Messages
    public static final Message PLAYER_CONFIG_LOADED = get("player-config-loaded");
    public static final Message PLAYER_CONFIG_NO_PLAYERS = get("player-config-no-players");
    public static final Message PLAYER_CONFIG_SAVING = get("player-config-saving");
    public static final Message PLAYER_CONFIG_SAVED = get("player-config-saved");

    // Logging Messages
    public static final Message VAULT_HOOK_SUCCESS = get("vault-hook-success");
    public static final Message VAULT_HOOK_FAILURE = get("vault-hook-failure");
    public static final Message PLUGIN_LOAD_SUCCESS = get("plugin-load-success");

    // Setting Messages
    public static final Message UPDATE_CHECKER_CHECKING = get("update-checker-checking");
    public static final Message UPDATE_CHECKER_BETA = get("update-checker-beta");
    public static final Message UPDATE_CHECKER_UP_TO_DATE = get("update-checker-up-to-date");
    public static final Message UPDATE_CHECKER_NOT_UP_TO_DATE = get("update-checker-not-up-to-date");
    public static final Message UPDATE_CHECKER_CURRENT_VERSION = get("update-checker-current-version");
    public static final Message UPDATE_CHECKER_AVAILABLE_VERSION = get("update-checker-available-version");
    public static final Message UPDATE_CHECKER_DOWNLOAD_SITE = get("update-checker-download-site");
    public static final Message UPDATE_CHECKER_DISABLED = get("update-checker-disabled");
    public static final Message UPDATE_CHECKER_FAILED = get("update-checker-failed");
    public static final Message UPDATE_CHECKER_PLAYER_AVAILABLE = get("update-checker-player-available");
    public static final Message UPDATE_CHECKER_PLAYER_DOWNLOAD = get("update-checker-player-download");


    private static Message get(String path) {
        return new Message(BeeConomy.getInstance().getMessageConfig().get(path));
    }

    private final String message;
    private String tempMessage;

    public Message(String message) {
        this.message = message;
    }

    private String getMessage() {
        String temp = tempMessage == null ? message : tempMessage;
        tempMessage = null;
        temp = temp.replace("<currency>", Config.ECO_NAME);
        temp = temp.replace("<currency-symbol>", Config.ECO_SYMBOL);
        return temp;
    }

    public void sendMessage(CommandSender sender) {
        Util.sendColMsg(sender, getMessage());
    }

    public void sendMessageNoPrx(CommandSender sender) {
        Util.sendColMsgNoPre(sender, getMessage());
    }

    public void log() {
        Util.log(getMessage());
    }

    public void warn() {
        Util.warn(getMessage());
    }

    public void error() {
        Util.error(getMessage());
    }

    private void replace(String regex, String replacement) {
        if (tempMessage == null) {
            tempMessage = message;
        }
        tempMessage = tempMessage.replaceFirst(regex, replacement);
    }

    public Message replacePlayer(OfflinePlayer player) {
        String name = player.getName();
        if (name == null) {
            name = player.getUniqueId().toString().substring(0, 10);
        }
        replace("<player>", name);
        return this;
    }

    public Message replacePlayer(String player) {
        replace("<player>", player);
        return this;
    }

    public Message replaceMoney(double money) {
        String s = String.format("%.2f", money);
        replace("<money>", s);
        return this;
    }

    public Message replaceNumber(long number) {
        replace("<number>", "" + number);
        return this;
    }

    public Message replaceString(String string) {
        replace("<string>", string);
        return this;
    }

}
