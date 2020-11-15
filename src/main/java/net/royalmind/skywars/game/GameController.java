package net.royalmind.skywars.game;

import net.daboross.bukkitdev.skywars.api.events.GameStartEvent;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.game.cages.Cages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameController implements Listener {

    private RoyalSkyWars royalSkyWars;

    public static final HashMap<Integer, GameTimer> GAMES_TIMER = new HashMap<>();

    public GameController(RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
    }

    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        final GameTimer gameTimer = new GameTimer(event.getNewGame(), royalSkyWars);
        final List<Player> players = event.getPlayers();
        final Cages cages = new Cages(royalSkyWars);
        final int gameId = event.getNewGame().getId();

        GAMES_TIMER.put(gameId, gameTimer);
        System.out.println("CREANDO CAJAS...");
        for (Player player : players) {
            cages.create(player, "default");
        }
        System.out.println("CAJAS CREADAS!");
        System.out.println("TIME DOWN..." + "(" + gameTimer.getTime() + ")");

        CompletableFuture.supplyAsync(() -> {
            return gameTimer.start();
        }).whenCompleteAsync((aVoid, throwable) -> {
            System.out.println(throwable.getMessage());
            try {
                GAMES_TIMER.remove(gameId);
                //TODO: REMOVE CAGES
                System.out.println("ELIMINANDO CAJAS!");
                System.out.println("Players size : " + players.size());
                for (Player player : players) {
                    System.out.println("CAJAS DE " + player.getName() + " ELIMINADA");
                    player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
                    //cages.delete(player);
                }
                System.out.println("CAJAS ELIMINADAS!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
