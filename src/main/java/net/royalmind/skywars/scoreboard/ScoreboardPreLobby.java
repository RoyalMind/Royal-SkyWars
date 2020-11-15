package net.royalmind.skywars.scoreboard;

import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.statistics.Stats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ScoreboardPreLobby implements ScoreboardHandler {

    private RoyalSkyWars royalSkyWars;
    private final FileConfiguration config;
    private HighlightedString title;

    public ScoreboardPreLobby(final RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
        this.config = royalSkyWars.getRoyalLocations().getConfig();
    }

    @Override
    public String getTitle(Player player) {
        title = new HighlightedString(
                getConfig().getString("Scoreboard.PreLobby.Title")
                , "&6&l", "&e&l"
        );
        return title.getContext();
    }

    @Override
    public List<Entry> getEntries(Player player) {
        final EntryBuilder entryBuilder = new EntryBuilder();
        for (String s : getConfig().getStringList("Scoreboard.PreLobby.Body")) {
            s = Stats.translate(royalSkyWars, player, s);
            if (s.isEmpty() || s.equalsIgnoreCase("%blank%")) {
                entryBuilder.blank();
                continue;
            }
            entryBuilder.next(s);
        }
        return entryBuilder.build();
    }

    /*private String translateVars(String text, final Player player) {
        final SkyInternalPlayer skyPlayer = getSkywars().getPlayers().getPlayer(player);

        text = text.replaceAll("", "");
        return text;
    }*/

    private FileConfiguration getConfig() {
        return config;
    }
}
