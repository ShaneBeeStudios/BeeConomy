package com.shanebeestudios.bc.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

public class UpdateChecker implements Listener {

    private static String UPDATE_VERSION;

    public static void checkForUpdate(String pluginVersion) {
        Message.UPDATE_CHECKER_CHECKING.log();
        if (pluginVersion.contains("-")) {
            Message.UPDATE_CHECKER_BETA.log();
            return;
        }
        getVersion(version -> {
            if (version.equalsIgnoreCase(pluginVersion)) {
                Message.UPDATE_CHECKER_UP_TO_DATE.log();
            } else {
                Message.UPDATE_CHECKER_NOT_UP_TO_DATE.log();
                Message.UPDATE_CHECKER_CURRENT_VERSION.replaceString(pluginVersion).log();
                Message.UPDATE_CHECKER_AVAILABLE_VERSION.replaceString(version).log();
                Message.UPDATE_CHECKER_DOWNLOAD_SITE.log();
                UPDATE_VERSION = version;
            }
        });
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    private static void getVersion(final Consumer<String> consumer) {
        try {
            // TODO URL is deprecated, replace with URI in future
            URL url = new URL("https://api.github.com/repos/ShaneBeeStudios/BeeConomy/releases/latest");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            String tag_name = jsonObject.get("tag_name").getAsString();
            consumer.accept(tag_name);
        } catch (IOException e) {
            if (Config.SETTINGS_DEBUG) {
                e.printStackTrace();
            } else {
                Message.UPDATE_CHECKER_FAILED.log();
            }
        }
    }

    private final BeeConomy PLUGIN;

    public UpdateChecker(BeeConomy plugin) {
        this.PLUGIN = plugin;

        String permString = "eco.update.check";
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        if (pluginManager.getPermission(permString) == null) {
            Permission permission = new Permission(permString);
            permission.setDefault(PermissionDefault.OP);
            pluginManager.addPermission(permission);
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (UPDATE_VERSION == null) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("eco.update.check")) return;

        Bukkit.getScheduler().runTaskLater(PLUGIN, bukkitTask -> {
            Message.UPDATE_CHECKER_PLAYER_AVAILABLE.replaceString(UPDATE_VERSION).sendMessage(player);
            Message.UPDATE_CHECKER_PLAYER_DOWNLOAD.sendMessage(player);
        }, 60);
    }
}
