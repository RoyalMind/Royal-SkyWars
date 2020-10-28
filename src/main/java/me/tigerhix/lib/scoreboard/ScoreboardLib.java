package me.tigerhix.lib.scoreboard;

import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.SimpleScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class ScoreboardLib {

    private static Plugin instance;

    public static Plugin getPluginInstance() {
        return instance;
    }

    public static void setPluginInstance(Plugin instance) {
        if (ScoreboardLib.instance != null) return;
        ScoreboardLib.instance = instance;
    }

    public static Scoreboard createScoreboard(Player holder) {
        return new SimpleScoreboard(holder);
    }

}