package net.royalmind.skywars.files;

import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.Location;

public class DataCache {

    private RoyalSkyWars royalSkyWars;
    private Location preLobby = null;

    public DataCache(final RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
        registerLocations();
    }

    private void registerLocations() {
        preLobby = ((Location) getRoyalSkyWars().getRoyalLocations().getConfig().get("PreLobby"));
    }

    public RoyalSkyWars getRoyalSkyWars() {
        return royalSkyWars;
    }

    public Location getPreLobby() {
        return preLobby;
    }

    public void setPreLobby(Location preLobby) {
        this.preLobby = preLobby;
    }
}
