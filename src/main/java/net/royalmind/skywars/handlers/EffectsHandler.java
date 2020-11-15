package net.royalmind.skywars.handlers;

import net.daboross.bukkitdev.skywars.api.events.ArenaPlayerDeathEvent;
import net.daboross.bukkitdev.skywars.api.events.GameEndEvent;
import net.daboross.bukkitdev.skywars.api.events.GameStartEvent;
import net.daboross.bukkitdev.skywars.api.events.PlayerEnterQueueEvent;
import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectsHandler implements Listener {

    private final RoyalSkyWars royalSkyWars;

    public EffectsHandler(final RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
    }

    @EventHandler
    public void onQueueJoin(final PlayerEnterQueueEvent event) {
        final Player player = event.getPlayer();
        effect(player, 1);
    }

    @EventHandler
    public void onGameJoin(final GameStartEvent event) {
        for (Player player : event.getPlayers()) {
            effect(player, 2);
        }
    }

    @EventHandler
    public void onGameLeave(final GameEndEvent event) {
        for (Player player : event.getAlivePlayers()) {
            effect(player, 1);
        }
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        effect(player, 2);
    }

    @EventHandler
    public void onPlayerDeath(final ArenaPlayerDeathEvent event) {
        final Location location = event.getKilled().getLocation();
        location.getWorld().strikeLightningEffect(location);
    }

    private void effect(final Player player, final int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (duration * 20), 1), true);
    }

}
