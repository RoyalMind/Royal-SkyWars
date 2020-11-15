package net.royalmind.skywars.commands;

import net.royalmind.skywars.Chat;
import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoyalCommands implements CommandExecutor {

    private RoyalSkyWars royalSkyWars;

    public RoyalCommands(RoyalSkyWars royalSkyWars) {
        this.royalSkyWars = royalSkyWars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.translate("&cNO PUEDES EJECUTAR ESTE COMANDO EN CONSOLA"));
            return true;
        }
        final Player player = (Player) sender;
        if (!(player.hasPermission("skywars.admin"))) return true;

        if (args.length < 1) {
            player.sendMessage(Chat.translate("&7- &e/rmsw set"));
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(Chat.translate("&7- &e/rmsw set prelobby"));
            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args[1].equalsIgnoreCase("prelobby")) {
                final Location location = player.getLocation();
                getRoyalSkyWars().getDataCache().setPreLobby(location);
                getRoyalSkyWars().getRoyalLocations().set("preLobby", location);
                player.sendMessage("Ubicacion guardada xd");
                return true;
            }
        }

        player.sendMessage(Chat.translate("(Final) &7- &e/rmsw set"));
        return true;
    }

    private RoyalSkyWars getRoyalSkyWars() {
        return royalSkyWars;
    }
}
