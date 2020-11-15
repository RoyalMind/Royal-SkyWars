package net.royalmind.skywars.statistics;

import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import net.daboross.bukkitdev.skywars.api.arenaconfig.SkyArena;
import net.daboross.bukkitdev.skywars.api.storage.SkyInternalPlayer;
import net.daboross.bukkitdev.skywars.game.ArenaGame;
import net.daboross.bukkitdev.skywars.game.GameQueue;
import net.daboross.bukkitdev.skywars.game.GameQueueTimer;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.game.GameController;
import net.royalmind.skywars.game.GameTimer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Stats {

    public static String translate(final RoyalSkyWars royalSkyWars, final Player player, String text) {
        final PlayerStats playerStats = PlayerStats.getPlayerStats().get(player);
        final SkyWarsPlugin skyWarsPlugin = royalSkyWars.getInstance();
        final SkyInternalPlayer skyPlayer = skyWarsPlugin.getPlayers().getPlayer(player);
        final ArenaGame game = skyWarsPlugin.getIDHandler().getGame(skyPlayer.getGameId());
        //final GameQueueTimer gameQueueTimer = skyWarsPlugin.getGameQueueTimer();

        String mode = "ERROR!";
        String mapName = "ERROR!";
        String totalPlayers = "ERROR!";
        String playersAlive = "ERROR!";
        String serverID = "ERROR!";
        String timeToStart = "ERROR!";

        //In queue
        final GameQueue gameQueue = skyWarsPlugin.getGameQueue();
        if (gameQueue.inQueue(player.getUniqueId())) {
            final SkyArena arena = skyWarsPlugin.getGameQueue().getPlannedArena();
            final GameQueueTimer gameQueueTimer = skyWarsPlugin.getGameQueueTimer();
            timeToStart = String.valueOf(gameQueueTimer.getTimeToStart());
            mapName = arena.getArenaName();
            playersAlive = String.valueOf(gameQueue.getNumPlayersInQueue());
            totalPlayers = String.valueOf(arena.getNumPlayers());

            if (!(gameQueue.areMinPlayersPresent())) {
                timeToStart = "30";
            }
        }

        //In game
        if (game != null) {
            mode = game.areTeamsEnabled() ? "Equipos" : "Solo";
            mapName = game.getArena().getArenaName();
            totalPlayers = String.valueOf(game.getArena().getNumPlayers());
            playersAlive = String.valueOf(game.getAlivePlayers().size());
            serverID = "MINISW-" + ((game.getId() < 9) ? "0" + game.getId() : String.valueOf(game.getId()));
            final HashMap<Integer, GameTimer> gamesTimer = GameController.GAMES_TIMER;

            if (GameController.GAMES_TIMER.containsKey(game.getId())) {
                timeToStart = String.valueOf(gamesTimer.get(game.getId()).getTime());
            }
        }

        final String kills = String.valueOf(playerStats.getKills());
        final String deaths = String.valueOf(playerStats.getDeaths());

        /*
            Se necesita una scoreboard para cuando sean equipos
            y no se agregaron esos statistics

            Ya esta la scoreboard pero falta agregar la traduccion de variables
         */

        text = text.replaceAll("%mode%", mode)
                .replaceAll("%map-name%", mapName)
                .replaceAll("%all-players%", totalPlayers)
                .replaceAll("%alive-players%", playersAlive)
                .replaceAll("%server-id%", serverID)
                .replaceAll("%kills%", kills)
                .replaceAll("%deaths%", deaths)
                .replaceAll("%time-start%", timeToStart);

        return text;
    }
}
