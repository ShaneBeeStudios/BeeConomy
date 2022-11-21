package com.shanebeestudios.bc.util;

import com.shanebeestudios.bc.config.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String CONSOLE_PREFIX = "&7[&bBee&3Conomy&7] ";
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");
    private static final int[] BUKKIT_VERSION = new int[3];

    static {
        String[] bukkitVersion = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.");
        BUKKIT_VERSION[0] = Integer.parseInt(bukkitVersion[0]);
        BUKKIT_VERSION[1] = Integer.parseInt(bukkitVersion[1]);
        try {
            BUKKIT_VERSION[2] = Integer.parseInt(bukkitVersion[2]);
        } catch (Exception ignore) {
            BUKKIT_VERSION[2] = 0;
        }
    }

    /**
     * Shortcut for adding color to a string
     * <p>Supports HEX color codes in 1.16+</p>
     *
     * @param string String including color codes
     * @return Formatted string
     */
    public static String getColString(String string) {
        if (isRunningMinecraft(1, 16)) {
            Matcher matcher = HEX_PATTERN.matcher(string);
            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String log) {
        Bukkit.getConsoleSender().sendMessage(getColString(CONSOLE_PREFIX + log));
    }

    public static void log(String format, Object... objects) {
        log(String.format(format, objects));
    }

    public static void warn(String warn) {
        log("&e" + warn);
    }

    public static void error(String error) {
        log("&c" + error);
    }

    public static void sendColMsg(CommandSender sender, String message) {
        if (message.isEmpty()) return;
        String prefix = CONSOLE_PREFIX;
        if (sender instanceof Player) {
            prefix = Config.PREFIX;
        }
        sender.sendMessage(Util.getColString(prefix + message));
    }

    public static void sendColMsg(CommandSender sender, String formatMsg, Object... objects) {
        sendColMsg(sender, String.format(formatMsg, objects));
    }

    public static void sendColMsgNoPre(CommandSender sender, String message) {
        sender.sendMessage(Util.getColString(message));
    }

    public static void sendColMsgNoPre(CommandSender sender, String formatMsg, Object... objects) {
        sendColMsgNoPre(sender, String.format(formatMsg, objects));
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major Major version to check (Most likely just going to be 1)
     * @param minor Minor version to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor) {
        return isRunningMinecraft(major, minor, 0);
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major    Major version to check (Most likely just going to be 1)
     * @param minor    Minor version to check
     * @param revision Revision to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor, int revision) {
        int maj = BUKKIT_VERSION[0];
        int min = BUKKIT_VERSION[1];
        int rev = BUKKIT_VERSION[2];
        return maj > major || min > minor || (min == minor && rev >= revision);
    }

}
