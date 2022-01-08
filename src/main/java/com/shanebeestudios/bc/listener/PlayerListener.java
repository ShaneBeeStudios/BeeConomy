package com.shanebeestudios.bc.listener;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final BeeConomy PLUGIN;

    public PlayerListener(BeeConomy plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        EconomyManager ecoManager = PLUGIN.getEcoManager();
        if (!ecoManager.hasAccount(player)) {
            ecoManager.createEconomyPlayerAccount(player);
        }
    }

}
