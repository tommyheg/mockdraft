package data.storage;

import pojos.Player;
import pojos.ScoreType;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;
    private String tableName = "standardPlayers";

    public SQLStorer(ScoreType scoreType, int leagueSize) {
        super(scoreType, leagueSize);
        if (scoreType == ScoreType.HALF) tableName = "halfPlayers";
        else if (scoreType == ScoreType.PPR) tableName = "pprPlayers";
    }

    //store players from ffc that have adp/stdev data
    @Override
    public void storePlayers(int limit) {
        List<Player> players = webScrapers.get(0).getPlayers(limit);

        if (connection == null) establishConnection();
        createDatabase();

        for (Player player : players) {
            addPlayer(player);
        }
    }

    //update the players with projections
    @Override
    public void updatePlayers(int limit) {
        List<Player> players = webScrapers.get(1).getPlayers(limit);

        if (connection == null) establishConnection();
        for (Player player : players) {
            updatePlayer(player);
        }
    }

    //add a player to the sql table. the projections will be 0
    private void addPlayer(Player player) {
        Map<String, Double> projections = player.getProjections();
        String s = "insert into " + tableName + " values (" +
                player.getRank() + ", \"" +
                player.getLastName() + "\", \"" +
                player.getFirstName() + "\", \"" +
                player.getPosition() + "\", \"" +
                player.getTeam() + "\", " +
                projections.get("Points") + ", " +
                projections.get("Rush Att") + ", " +
                projections.get("Rush Yds") + ", " +
                projections.get("Rush Tds") + ", " +
                projections.get("Recs") + ", " +
                projections.get("Rec Yds") + ", " +
                projections.get("Rec Tds") + ", " +
                projections.get("Pass Cmp") + ", " +
                projections.get("Pass Att") + ", " +
                projections.get("Pass Yds") + ", " +
                projections.get("Pass Tds") + ", " +
                projections.get("Pass Ints") + ", " +
                projections.get("Fumbles") + ", " +
                player.getADP() + ", " +
                player.getSDEV() + ", \"" +
                player.getName() +
                "\");";
        try {
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //update the player in the table with their projections
    private void updatePlayer(Player player) {
        Map<String, Double> projections = player.getProjections();
        String fullName = player.getName();
        String first = player.getFirstName();
        String last = player.getLastName();
        String s = "update " + tableName + " set " +
                "Points = " + projections.get("Points") + ", " +
                "RushAtt = " + projections.get("Rush Att") + ", " +
                "RushYds = " + projections.get("Rush Yds") + ", " +
                "RushTds = " + projections.get("Rush Tds") + ", " +
                "Recs = " + projections.get("Recs") + ", " +
                "RecYds = " + projections.get("Rec Yds") + ", " +
                "RecTds = " + projections.get("Rec Tds") + ", " +
                "PassComp = " + projections.get("Pass Cmp") + ", " +
                "PassAtt = " + projections.get("Pass Att") + ", " +
                "PassYds = " + projections.get("Pass Yds") + ", " +
                "PassTds = " + projections.get("Pass Tds") + ", " +
                "PassInts = " + projections.get("Pass Ints") + ", " +
                "Fumbles = " + projections.get("Fumbles") +
                " where " +
                "FullName = \"" + fullName + "\" " +
                "or (FirstName = \"" + first + "\" " +
                "and LastName = \"" + last + "\");";
        try {
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void createDatabase() {
        String drop = "drop table if exists " + tableName + ";";
        String s = "create table " + tableName + "(\n" +
                "\tID int(3) PRIMARY KEY NOT NULL,\n" +
                "    LastName VarChar(255) NOT NULL,\n" +
                "    FirstName VarChar(255) NOT NULL,\n" +
                "    Position VarChar(5) NOT NULL,\n" +
                "    Team VarChar(5) NOT NULL,\n" +
                "    \n" +
                "\tPoints decimal(4, 1),\n" +
                "    RushAtt decimal(4, 1),\n" +
                "    RushYds decimal(5, 1),\n" +
                "    RushTds decimal(3, 1),\n" +
                "    Recs decimal(4, 1),\n" +
                "    RecYds decimal(5, 1),\n" +
                "    RecTds decimal(3, 1),\n" +
                "    PassComp decimal(4, 1),\n" +
                "    PassAtt decimal(4, 1),\n" +
                "    PassYds decimal(5, 1),\n" +
                "    PassTds decimal(3, 1),\n" +
                "    PassInts decimal(3, 1),\n" +
                "    Fumbles decimal(3, 1),\n" +
                "    ADP decimal(10,3),\n" +
                "    SDEV decimal(10,3),\n" +
                "    FullName VarChar(255) NOT NULL\n" +
                ");\n" +
                "\n";
        try {
            statement.execute(drop);
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void establishConnection() {
        //local server for now
        String url = "jdbc:mysql://localhost:3306/mockdraft?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void cleanUp() {
        closeConnection();
    }

}
