package net.royalmind.skywars.handlers;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import net.daboross.bukkitdev.skywars.api.events.GameStartEvent;
import net.daboross.bukkitdev.skywars.api.events.LeaveGameEvent;
import net.daboross.bukkitdev.skywars.api.events.PlayerEnterQueueEvent;
import net.daboross.bukkitdev.skywars.api.events.PlayerLeaveQueueEvent;
import net.daboross.bukkitdev.skywars.api.game.LeaveGameReason;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.scoreboard.ScoreboardInGame;
import net.royalmind.skywars.scoreboard.ScoreboardInTeamsGame;
import net.royalmind.skywars.scoreboard.ScoreboardLobby;
import net.royalmind.skywars.scoreboard.ScoreboardPreLobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class ScoreboardHandler implements Listener {

    private RoyalSkyWars royalSkyWars;

    private HashMap<Player, Scoreboard> scoreboards = new HashMap<>();

    public ScoreboardHandler(RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Scoreboard scoreboard = ScoreboardLib.createScoreboard(player);
        scoreboard.setHandler(new ScoreboardLobby(royalSkyWars));
        scoreboard.activate();
        getScoreboards().put(player, scoreboard);
    }

    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (!(getScoreboards().containsKey(player))) return;
        getScoreboards().remove(player);
    }

    @EventHandler
    public void onPreLobbyJoin(final PlayerEnterQueueEvent event) {
        final Player player = event.getPlayer();
        if (getScoreboards().containsKey(player)) {
            getScoreboards().get(player).setHandler(new ScoreboardPreLobby(royalSkyWars));
        }
    }

    @EventHandler
    public void onPreLobbyLeave(final PlayerLeaveQueueEvent event) {
        final Player player = event.getPlayer();
        if (getScoreboards().containsKey(player)) {
            getScoreboards().get(player).setHandler(new ScoreboardLobby(royalSkyWars));
        }
    }

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        for (Player player : event.getPlayers()) {
            if (getScoreboards().containsKey(player)) {
                if (event.getNewGame().areTeamsEnabled()) {
                    getScoreboards().get(player).setHandler(new ScoreboardInTeamsGame(royalSkyWars));
                    return;
                }
                getScoreboards().get(player).setHandler(new ScoreboardInGame(royalSkyWars));
            }
        }
    }

    /*@EventHandler
    public void onGameEnd(final GameEndEvent event) {
        for (Player player : event.getAlivePlayers()) {
            if (getScoreboards().containsKey(player)) {
                getScoreboards().get(player).setHandler(new ScoreboardLobby(royalSkyWars));
            }
        }
    }*/

    @EventHandler
    public void leaveGame(final LeaveGameEvent event) {
        final Player player = event.getPlayer();
        if (event.getReason() == LeaveGameReason.DISCONNECTED) return;
        if (getScoreboards().containsKey(player)) {
            getScoreboards().get(player).setHandler(new ScoreboardLobby(royalSkyWars));
        }
    }

    public HashMap<Player, Scoreboard> getScoreboards() {
        return scoreboards;
    }
}
