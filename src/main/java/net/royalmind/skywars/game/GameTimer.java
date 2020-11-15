package net.royalmind.skywars.game;

import net.daboross.bukkitdev.skywars.api.game.SkyGame;
import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer {

    private int time;
    private SkyGame skyGame;
    private final RoyalSkyWars royalSkyWars;

    public GameTimer(final SkyGame skyGame, final RoyalSkyWars royalSkyWars) {
        final FileConfiguration config = royalSkyWars.getRoyalLocations().getConfig();
        this.skyGame = skyGame;
        this.royalSkyWars = royalSkyWars;
        this.time = config.getInt("Game.Cages.Time");
    }

    public boolean start() {
        System.out.println("INICIANDO GAME TIMER (START)");
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("TIMER : " + time);
                if (time <= 0) {
                    cancel();
                }
                time--;
            }
        }.runTaskTimer(royalSkyWars.getInstance(), 0L, 20L);
        System.out.println("TERMINANDO GAME TIMER (START)");
        return true;
    }

    public int getTime() {
        return time;
    }

    public SkyGame getSkyGame() {
        return skyGame;
    }
}
