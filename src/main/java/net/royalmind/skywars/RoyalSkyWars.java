package net.royalmind.skywars;

import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import net.royalmind.skywars.commands.RoyalCommands;
import net.royalmind.skywars.database.SQLStorage;
import net.royalmind.skywars.files.DataCache;
import net.royalmind.skywars.files.RoyalLocations;
import net.royalmind.skywars.game.GameController;
import net.royalmind.skywars.game.cages.Schematic;
import net.royalmind.skywars.handlers.EffectsHandler;
import net.royalmind.skywars.handlers.ItemHandler;
import net.royalmind.skywars.handlers.ScoreboardHandler;
import net.royalmind.skywars.handlers.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.nio.file.Path;

public class RoyalSkyWars {

    private SkyWarsPlugin instance;
    private RoyalLocations royalLocations;
    private DataCache dataCache;
    private SQLStorage storage;
    private Schematic schematicCages;

    public RoyalSkyWars(final SkyWarsPlugin instance) {
        this.instance = instance;
        registerHandlers();
        loadFiles();
        registerCommands();
        dataCache = new DataCache(this);
        schematicCages = new Schematic("cages", instance);
    }

    private void loadFiles() {
        //RoyalLocations
        this.royalLocations = preloadFile("royal");
    }

    private RoyalLocations preloadFile(final String name) {
        Path configFile = instance.getDataFolder().toPath().resolve(name + ".yml");
        RoyalLocations config = new RoyalLocations(configFile, instance.getLogger());
        try {
            config.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return config;
    }

    private void registerHandlers() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        //Game
        pluginManager.registerEvents(new GameController(this), instance);

        pluginManager.registerEvents(new ItemHandler(this, instance), instance);
        pluginManager.registerEvents(new ScoreboardHandler(this), instance);
        pluginManager.registerEvents(new EffectsHandler(this), instance);

        try {
            storage = new SQLStorage(this);
            storage.createTable();
            pluginManager.registerEvents(new StorageHandler(this, storage), instance);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerCommands() {
        Bukkit.getServer().getPluginCommand("rmskywars").setExecutor(new RoyalCommands(this));
    }

    public SkyWarsPlugin getInstance() {
        return instance;
    }

    public RoyalLocations getRoyalLocations() {
        return royalLocations;
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    public SQLStorage getStorage() {
        return storage;
    }

    public Schematic getSchematicCages() {
        return schematicCages;
    }
}
