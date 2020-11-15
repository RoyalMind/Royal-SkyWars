package net.royalmind.skywars.game.cages;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Cages {

    private RoyalSkyWars royalSkyWars;

    public Cages(RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
    }

    public void save(final Player player, final String name) {
        final WorldEditPlugin worldEdit = ((WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit"));
        final Selection selection = worldEdit.getSelection(player);
        final Schematic schematicCages = royalSkyWars.getSchematicCages();
        if (selection == null) {
            player.sendMessage("Que quieres que guarde?, si no has seleccionado nada con WorldEdit maldito xdd");
            return;
        }
        schematicCages.save(player, name);
    }

    public void create(final Player player, final String name) {
        final Schematic schematicCages = royalSkyWars.getSchematicCages();
        schematicCages.paste(name, player.getLocation());
    }

    public void delete(final Player player) {
        final int radius = royalSkyWars.getRoyalLocations().getConfig().getInt("Game.Cages.DeletionRadius");
        final Location location = player.getLocation();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    final Block blockAt = location.getWorld().getBlockAt(x, y, z);
                    if (blockAt.getType() == Material.AIR) continue;
                    blockAt.setType(Material.AIR);
                }
            }
        }
    }
}
