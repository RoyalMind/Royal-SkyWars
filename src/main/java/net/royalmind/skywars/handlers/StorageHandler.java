package net.royalmind.skywars.handlers;

import net.daboross.bukkitdev.skywars.api.events.ArenaPlayerDeathEvent;
import net.daboross.bukkitdev.skywars.api.events.ArenaPlayerKillPlayerEvent;
import net.daboross.bukkitdev.skywars.api.events.GameEndEvent;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.database.SQLCallback;
import net.royalmind.skywars.database.SQLStorage;
import net.royalmind.skywars.statistics.PlayerStats;
import net.royalmind.skywars.statistics.PlayerStatsTypes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;

public class StorageHandler implements Listener {

    private final RoyalSkyWars royalSkyWars;
    private final SQLStorage storage;

    public StorageHandler(RoyalSkyWars royalSkyWars, SQLStorage storage) {
        this.royalSkyWars = royalSkyWars;
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        storage.createPlayerData(player);
        //Cache
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        if (playerStats.containsKey(player)) return;
        playerStats.put(player, new PlayerStats(player, royalSkyWars));
    }

    @EventHandler
    public void onPlayerKill(final ArenaPlayerKillPlayerEvent event) {
        final Player killer = event.getKilled().getKiller();

        /*storage.getStatistic(killed, "deaths", new SQLCallback() {
            @Override
            public void dataGetCallback(final Object object) {
                storage.updateStatistics(killed, "deaths", ((Integer) object) + 1);
            }
        });*/

        storage.getStatistic(killer, "kills", new SQLCallback() {
            @Override
            public void dataGetCallback(final Object object) {
                storage.updateStatistics(killer, "kills", ((Integer) object) + 1);
            }
        });
        //Cache
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        if (!(playerStats.containsKey(killer))) return;
        playerStats.get(killer).addOne(PlayerStatsTypes.KILLS);
    }

    @EventHandler
    public void onPlayerDeath(final ArenaPlayerDeathEvent event) {
        final Player killed = event.getKilled();
        storage.getStatistic(killed, "deaths", new SQLCallback() {
            @Override
            public void dataGetCallback(final Object object) {
                storage.updateStatistics(killed, "deaths", ((Integer) object) + 1);
            }
        });
        //Cache
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        if (!(playerStats.containsKey(killed))) return;
        playerStats.get(killed).addOne(PlayerStatsTypes.DEATHS);
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        //Cache
        final Player player = event.getPlayer();
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        if (!(playerStats.containsKey(player))) return;
        playerStats.remove(player);
    }

    @EventHandler
    public void onPlayerWin(final GameEndEvent event) {
        final List<Player> alivePlayers = event.getAlivePlayers();
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        for (final Player player : alivePlayers) {
            if (!(playerStats.containsKey(player))) continue;
            storage.getStatistic(player, "wins", new SQLCallback() {
                @Override
                public void dataGetCallback(final Object object) {
                    storage.updateStatistics(player, "wins", ((Integer) object) + 1);
                }
            });
            //Cache
            playerStats.get(player).addOne(PlayerStatsTypes.WINS);
        }
    }

    @EventHandler
    public void onPlayerLose(final ArenaPlayerDeathEvent event) {
        final Player player = event.getKilled();
        final HashMap<Player, PlayerStats> playerStats = PlayerStats.getPlayerStats();
        if (!(playerStats.containsKey(player))) return;
        storage.getStatistic(player, "loses", new SQLCallback() {
            @Override
            public void dataGetCallback(final Object object) {
                storage.updateStatistics(player, "loses", ((Integer) object) + 1);
            }
        });
        //Cache
        playerStats.get(player).addOne(PlayerStatsTypes.LOSES);
    }

    public RoyalSkyWars getRoyalSkyWars() {
        return royalSkyWars;
    }
}
