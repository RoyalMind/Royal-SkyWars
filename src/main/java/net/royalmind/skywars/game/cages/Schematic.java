package net.royalmind.skywars.game.cages;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class Schematic {

    private final String folderName;
    private final SkyWarsPlugin skyWarsPlugin;

    public Schematic(String folderName, SkyWarsPlugin skyWarsPlugin) {
        this.folderName = folderName;
        this.skyWarsPlugin = skyWarsPlugin;
    }

    public void save(final Player player, final String schematicName) {
        try {
            File schematic = new File(skyWarsPlugin.getDataFolder(), "/" + folderName + "/" + schematicName + ".schematic");
            File dir = new File(skyWarsPlugin.getDataFolder(), "/" + folderName + "/");
            if (!dir.exists())
                dir.mkdirs();

            WorldEditPlugin wep = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            WorldEdit we = wep.getWorldEdit();

            LocalPlayer localPlayer = wep.wrapPlayer(player);
            LocalSession localSession = we.getSession(localPlayer);
            ClipboardHolder selection = localSession.getClipboard();
            EditSession editSession = localSession.createEditSession(localPlayer);

            Vector min = selection.getClipboard().getMinimumPoint();
            Vector max = selection.getClipboard().getMaximumPoint();

            editSession.enableQueue();
            CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)), min);
            clipboard.copy(editSession);
            SchematicFormat.MCEDIT.save(clipboard, schematic);
            editSession.flushQueue();

            player.sendMessage("Schematic guardada!");
        } catch (IOException | DataException ex) {
            ex.printStackTrace();
        } catch (EmptyClipboardException ex) {
            ex.printStackTrace();
        }
    }


    public void paste(final String schematicName, final Location pasteLoc) {
        try {
            File dir = new File(skyWarsPlugin.getDataFolder(), "/" + folderName + "/" + schematicName + ".schematic");

            EditSession editSession = new EditSession(new BukkitWorld(pasteLoc.getWorld()), 999999999);
            editSession.enableQueue();

            SchematicFormat schematic = SchematicFormat.getFormat(dir);
            CuboidClipboard clipboard = schematic.load(dir);

            clipboard.paste(editSession, BukkitUtil.toVector(pasteLoc), true);
            editSession.flushQueue();
        } catch (DataException | IOException ex) {
            ex.printStackTrace();
        } catch (MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }
    }

    public String getFolderName() {
        return folderName;
    }
}