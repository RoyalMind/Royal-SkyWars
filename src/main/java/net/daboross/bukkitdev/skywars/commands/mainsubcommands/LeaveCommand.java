/*
 * Copyright (C) 2013-2014 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.skywars.commands.mainsubcommands;

import net.daboross.bukkitdev.commandexecutorbase.SubCommand;
import net.daboross.bukkitdev.commandexecutorbase.filters.ArgumentFilter;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import net.daboross.bukkitdev.skywars.api.game.LeaveGameReason;
import net.daboross.bukkitdev.skywars.api.translations.SkyTrans;
import net.daboross.bukkitdev.skywars.api.translations.TransKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends SubCommand {

    private final SkyWars plugin;

    public LeaveCommand(SkyWars plugin) {
        super("leave", false, "skywars.leave", SkyTrans.get(TransKey.CMD_LEAVE_DESCRIPTION));
        this.addCommandFilter(new ArgumentFilter(ArgumentFilter.ArgumentCondition.EQUALS, 0, SkyTrans.get(TransKey.TOO_MANY_PARAMS)));
        this.plugin = plugin;
    }

    @Override
    public void runCommand(CommandSender sender, Command baseCommand, String baseCommandLabel, String subCommandLabel, String[] subCommandArgs) {
        if (plugin.getGameQueue().inQueue(((Player) sender).getUniqueId())) {
            plugin.getGameQueue().removePlayer((Player) sender);
            ((Player) sender).teleport(plugin.getLocationStore().getLobbyPosition().toLocation());
            sender.sendMessage(SkyTrans.get(TransKey.CMD_LEAVE_REMOVED_FROM_QUEUE));
        } else if (plugin.getGameQueue().inSecondaryQueue(((Player) sender).getUniqueId())) {
            plugin.getGameQueue().removePlayer((Player) sender);
            sender.sendMessage(SkyTrans.get(TransKey.CMD_LEAVE_REMOVED_FROM_SECONDARY_QUEUE));
        } else if (plugin.getCurrentGameTracker().isInGame(((Player) sender).getUniqueId())) {
            plugin.getGameHandler().removePlayerFromGame((Player) sender, LeaveGameReason.LEAVE_COMMAND, true, true);
            sender.sendMessage(SkyTrans.get(TransKey.CMD_LEAVE_REMOVED_FROM_GAME));
        } else {
            sender.sendMessage(SkyTrans.get(TransKey.CMD_LEAVE_NOT_IN));
        }
    }
}
