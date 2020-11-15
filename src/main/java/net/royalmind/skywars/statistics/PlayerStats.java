package net.royalmind.skywars.statistics;

import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.database.SQLStorage;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashMap;

public class PlayerStats {

    private static final HashMap<Player, PlayerStats> PLAYER_STATS = new HashMap<>();

    private final Player player;
    private int kills = 0,
                deaths = 0,
                wins = 0,
                loses = 0;
    private final RoyalSkyWars royalSkyWars;

    public PlayerStats(final Player player, final RoyalSkyWars royalSkyWars) {
        this.player = player;
        this.royalSkyWars = royalSkyWars;
        loadPlayerStats();
    }

    private void loadPlayerStats() {
        final SQLStorage storage = royalSkyWars.getStorage();
        final ResultSet allStatistic = storage.getAllStatistic(player);
        try {
             kills = allStatistic.getInt("kills");
             deaths = allStatistic.getInt("deaths");
             wins = allStatistic.getInt("wins");
             loses = allStatistic.getInt("loses");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addOne(final PlayerStatsTypes playerStatsTypes) {
        switch (playerStatsTypes) {
            case KILLS:
                kills++;
                break;
            case DEATHS:
                deaths++;
                break;
            case WINS:
                wins++;
                break;
            case LOSES:
                loses++;
                break;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public RoyalSkyWars getRoyalSkyWars() {
        return royalSkyWars;
    }

    public static HashMap<Player, PlayerStats> getPlayerStats() {
        return PLAYER_STATS;
    }
}
