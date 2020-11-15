package net.royalmind.skywars.handlers;

import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import net.daboross.bukkitdev.skywars.api.events.GameEndEvent;
import net.daboross.bukkitdev.skywars.api.events.PlayerEnterQueueEvent;
import net.daboross.bukkitdev.skywars.api.events.PlayerLeaveQueueEvent;
import net.daboross.bukkitdev.skywars.api.translations.SkyTrans;
import net.daboross.bukkitdev.skywars.api.translations.TransKey;
import net.royalmind.skywars.Chat;
import net.royalmind.skywars.RoyalSkyWars;
import net.royalmind.skywars.items.ItemFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ItemHandler implements Listener {

    private RoyalSkyWars royalSkyWars;
    private SkyWarsPlugin skyWarsPlugin;

    private final Material itemKits = Material.NETHER_STAR;
    private final Material itemEventos = Material.BLAZE_POWDER;
    private final Material itemSalir = Material.BARRIER;

    public ItemHandler(final RoyalSkyWars royalSkyWars, final SkyWarsPlugin skyWarsPlugin) {
        this.royalSkyWars = royalSkyWars;
        this.skyWarsPlugin = skyWarsPlugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void sendToWaitLobby(final PlayerEnterQueueEvent event) {
        final Player player = event.getPlayer();
        player.closeInventory();
        final Location preLobby = royalSkyWars.getDataCache().getPreLobby();
        if (preLobby == null) {
            for (int i = 0; i < 15; i++) {
                player.sendMessage(Chat.translate("&cNo colocaste el prelobby maldito xdd"));
            }
            player.sendMessage(Chat.translate("&c&lY ya sabia que ibas a ser tu " + player.getName() + " xDDD"));
            return;
        }
        player.teleport(preLobby);
    }

    @EventHandler
    public void giveItemsOnJoin(final PlayerEnterQueueEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final ItemStack kits = new ItemFactory("&aKits", getItemKits()).getFinalItem();
                //final ItemStack events = new ItemFactory("&eEventos", getItemEventos()).getFinalItem();
                final ItemStack leave = new ItemFactory("&cSalir", getItemSalir()).getFinalItem();
                final Player player = event.getPlayer();

                player.getInventory().setItem(0, kits);
                //player.getInventory().setItem(7, events);
                player.getInventory().setItem(8, leave);
            }
        }.runTaskLater(getSkyWarsPlugin(), 2L);
    }

    @EventHandler
    public void removeItemsOnLeave(final PlayerLeaveQueueEvent event) {
        final Player player = event.getPlayer();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    @EventHandler
    public void dropInQueue(final PlayerDropItemEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();
        if (getSkyWarsPlugin().getGameQueue().inQueue(uniqueId)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getItem() == null) return;
        final Material itemType = event.getItem().getType();
        if (itemType == Material.AIR || !(getSkyWarsPlugin().getGameQueue().inQueue(player.getUniqueId()))) return;
        if (itemType == getItemEventos()) {
            // 0u0 \\
        } else if (itemType == getItemKits()) {
            getSkyWarsPlugin().getKitGui().openKitGui(player);
        } else if (itemType == getItemSalir()) {
            getSkyWarsPlugin().getGameQueue().removePlayer(player);
            player.teleport(getSkyWarsPlugin().getLocationStore().getLobbyPosition().toLocation());
            player.sendMessage(SkyTrans.get(TransKey.CMD_LEAVE_REMOVED_FROM_QUEUE));
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        new BukkitRunnable() {
            @Override
            public void run() {
                resetPlayerLocation(player);
            }
        }.runTaskLater(royalSkyWars.getInstance(), 2L);
    }

    @EventHandler
    public void onGameEnd(final GameEndEvent event) {
        for (Player player : event.getAlivePlayers()) {
            resetPlayerLocation(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        resetPlayerLocation(player);
    }

    private void resetPlayerLocation(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20L);
        player.teleport(getSkyWarsPlugin().getLocationStore().getLobbyPosition().toLocation());
    }

    private SkyWarsPlugin getSkyWarsPlugin() {
        return skyWarsPlugin;
    }

    public Material getItemKits() {
        return itemKits;
    }

    public Material getItemEventos() {
        return itemEventos;
    }

    public Material getItemSalir() {
        return itemSalir;
    }
}
