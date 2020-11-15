package net.royalmind.skywars.database;

import net.daboross.bukkitdev.asyncsql.*;
import net.daboross.bukkitdev.skywars.SkyWarsPlugin;
import net.daboross.bukkitdev.skywars.api.config.SkyConfiguration;
import net.royalmind.skywars.RoyalSkyWars;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLStorage {

    private final RoyalSkyWars royalSkyWars;
    private static final String TABLE_NAME = "skywars_stats";
    private final AsyncSQL sql;
    private Connection connection;
    private final SQLConnectionInfo connectionInfo;

    public SQLStorage(RoyalSkyWars royalSkyWars) throws SQLException {
        this.royalSkyWars = royalSkyWars;
        final SkyWarsPlugin instance = this.royalSkyWars.getInstance();
        final SkyConfiguration config = instance.getConfiguration();
        this.connectionInfo = new SQLConnectionInfo(config.getScoreSqlHost(), config.getScoreSqlPort(),
                config.getScoreSqlDatabase(), config.getScoreSqlUsername(), config.getScoreSqlPassword());
        this.sql = new AsyncSQL(instance, instance.getLogger(), connectionInfo);
        createConnection();
    }

    public void createTable() {
        sql.run("create user table", new SQLRunnable() {
            @Override
            public void run(final Connection connection) throws SQLException {
                try (final PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` (`uuid` VARCHAR(36), `username` VARCHAR(32), `kills` INT, `deaths` INT, `wins` INT, `loses` INT, PRIMARY KEY (`uuid`));"
                )) {
                    statement.execute();
                }
            }
        });
    }

    public void createPlayerData(final Player player) {
        sql.run("create player data", new SQLRunnable() {
            @Override
            public void run(final Connection connection) throws SQLException {
                try (final PreparedStatement statement = connection.prepareStatement(
                        "INSERT IGNORE INTO `" + TABLE_NAME + "` (uuid, username, kills, deaths, wins, loses) VALUES " +
                                "(?, ?, ?, ?, ?, ?);"
                )) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setString(2, player.getName());
                    statement.setInt(3, 0);
                    statement.setInt(4, 0);
                    statement.setInt(5, 0);
                    statement.setInt(6, 0);
                    statement.execute();
                }
            }
        });
    }

    public void updateStatistics(final Player player, final String field, final Object value) {
        sql.run("update player data", new SQLRunnable() {
            @Override
            public void run(final Connection connection) throws SQLException {
                try (final PreparedStatement statement = connection.prepareStatement(
                        "UPDATE `" + TABLE_NAME + "` SET " +
                                "`" + field + "` = '" + value + "' " +
                                "WHERE uuid = '" + player.getUniqueId().toString() + "';"
                )) {
                    statement.execute();
                }
            }
        });
    }

    public void getStatistic(final Player player, final String field, final SQLCallback sqlCallback) {
        sql.run("get player data", new ResultSQLRunnable<Object>() {
            @Override
            public void run(final Connection connection, final ResultHolder<Object> resultHolder) throws SQLException {
                try (final PreparedStatement statement = connection.prepareStatement(
                        "SELECT " + field + " FROM `" + TABLE_NAME + "` WHERE uuid = '" + player.getUniqueId().toString() + "';"
                )) {
                    try (ResultSet set = statement.executeQuery()) {
                        if (set.next()) {
                            resultHolder.set(set.getObject(field));
                        }
                    }
                }
            }
        }, new ResultRunnable<Object>() {
            @Override
            public void runWithResult(final Object o) {
                sqlCallback.dataGetCallback(o == null ? 0 : o);
            }
        });
    }

    /*public void getAllStatistic(final Player player, final SQLCallback sqlCallback) {
        sql.run("get player data", new ResultSQLRunnable<Object>() {
            @Override
            public void run(final Connection connection, final ResultHolder<Object> resultHolder) throws SQLException {
                try (final PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM `" + TABLE_NAME + "` WHERE uuid = '" + player.getUniqueId().toString() + "';"
                )) {
                    try (ResultSet set = statement.executeQuery()) {
                        if (set.next()) {
                            resultHolder.set(set);
                        }
                    }
                }
            }
        }, new ResultRunnable<Object>() {
            @Override
            public void runWithResult(final Object o) {
                sqlCallback.dataGetCallback(o == null ? 0 : o);
            }
        });
    }*/

    public ResultSet getAllStatistic(final Player player) {
        final String query = "SELECT * FROM `" + TABLE_NAME + "` WHERE uuid = '" + player.getUniqueId().toString() + "';";
        try {
            final PreparedStatement statement = connection.prepareStatement(query);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected void createConnection() {
        try {
            this.connection = connectionInfo.createConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
