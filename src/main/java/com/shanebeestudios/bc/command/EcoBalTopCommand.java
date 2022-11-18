package com.shanebeestudios.bc.command;

import com.shanebeestudios.bc.BeeConomy;
import com.shanebeestudios.bc.eco.EconomyManager;
import com.shanebeestudios.bc.eco.EconomyPlayer;
import com.shanebeestudios.bc.util.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class EcoBalTopCommand extends EcoBaseCmd {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("hh:mm a");
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    private final BeeConomy PLUGIN;
    private List<String> SORTED_PLAYERS;
    private List<Double> SORTED_BALANCES;
    private String TIME;

    public EcoBalTopCommand() {
        super("balancetop", "baltop", false);
        this.usage = "balancetop";
        this.PLUGIN = BeeConomy.getInstance();
        setDefaultPerm("eco.command.balancetop.pages", false);
    }

    @Override
    public boolean run() {
        int page = 1;
        if (args.length > 0 && NumberUtils.isNumber(args[0]) && sender.hasPermission("eco.command.balancetop.pages")) {
            page = Integer.parseInt(args[0]);
        }
        if (SORTED_BALANCES == null) {
            SORTED_BALANCES = new ArrayList<>();
            SORTED_PLAYERS = new ArrayList<>();
            EconomyManager ecoManager = PLUGIN.getEcoManager();
            Collection<EconomyPlayer> allEcoPlayers = ecoManager.getAllEcoPlayers();
            Map<String, Double> ecoPlayerBalMap = new HashMap<>();

            // Sort on another thread to prevent overloading server
            int finalPage = page;
            SCHEDULER.runTaskAsynchronously(PLUGIN, () -> {
                allEcoPlayers.forEach(economyPlayer -> {
                    String name = economyPlayer.getName();
                    if (name == null) {
                        name = economyPlayer.getUuid().toString().substring(0, 10);
                    }
                    ecoPlayerBalMap.put(name, economyPlayer.getBalance());
                });

                sortBalances(ecoPlayerBalMap, SORTED_BALANCES, SORTED_PLAYERS);
                TIME = DTF.format(LocalDateTime.now());

                // Send messages back on main thread
                SCHEDULER.runTaskLater(PLUGIN, () -> sendBalances(finalPage), 0);
                SCHEDULER.runTaskLater(PLUGIN, this::resetBalances, 100);
            });
        } else {
            sendBalances(page);
        }
        return true;
    }

    private void sortBalances(Map<String, Double> map, List<Double> balances, List<String> players) {
        balances.clear();
        players.clear();
        for (Map.Entry<String, Double> sortingMap : entriesSortedByValues(map)) {
            balances.add(sortingMap.getValue());
            players.add(sortingMap.getKey());
        }
    }

    private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
                (Map.Entry<K, V> e2, Map.Entry<K, V> e1) -> {
                    int res = e1.getValue().compareTo(e2.getValue());
                    if (res == 0) return 1;
                    else return res;
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    private void sendBalances(int page) {
        if (SORTED_PLAYERS.isEmpty()) {
            Message.CMD_BAL_TOP_BALANCE_SORTING.sendMessage(sender);
            return;
        }
        Message.CMD_BAL_TOP_HEADER.replaceString(TIME).sendMessage(sender);
        int totalPlayers = SORTED_PLAYERS.size();
        if (totalPlayers == 0) {
            return;
        }
        int totalPages = (int) Math.ceil((double) totalPlayers / 10);
        if (page > totalPages) {
            page = totalPages;
        }
        Message.CMD_BAL_TOP_PAGE
                .replaceNumber(page)
                .replaceNumber(totalPages)
                .sendMessageNoPrx(sender);
        int size = Math.min(totalPlayers, (page * 10));
        for (int i = (page * 10) - 10; i < size; i++) {
            Message.CMD_BAL_TOP_BALANCE.replaceNumber(i + 1)
                    .replacePlayer(SORTED_PLAYERS.get(i))
                    .replaceMoney(SORTED_BALANCES.get(i))
                    .sendMessageNoPrx(sender);
        }
    }

    private void resetBalances() {
        SORTED_BALANCES = null;
        SORTED_PLAYERS = null;
    }

}
