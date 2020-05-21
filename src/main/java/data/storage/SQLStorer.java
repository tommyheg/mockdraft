package data.storage;

import pojos.Player;
import pojos.ScoreType;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;
    private String tableName, copyName;

    public SQLStorer(ScoreType scoreType, int leagueSize){

        super(scoreType, leagueSize);
        switch(scoreType){
            case STANDARD: tableName = "standardPlayers"; copyName = "standardCopy"; break;
            case HALF: tableName = "halfPlayers"; copyName = "halfCopy"; break;
            case PPR: tableName = "pprPlayers"; copyName = "pprCopy"; break;
        }
//        tableName = "players";
    }

    /**
     * Store the list of players from the website in an sql database
     */
    @Override
    public void storeData(int limit) {
        //get the list of players and associated data from web scraping from FP (can be changed in future)
        List<Player> players = webScrapers.get(0).getPlayers(limit);

        establishConnection();  //establish connection to the sql database
        createDatabase();        //first clear and create database

        for (Player player : players) {
            addPlayer(player);
        }

    }

    /**
     * Update the table with each player's projections
     * @param limit- number of players to get (will be removed soon)
     */
    @Override
    public void updateData(int limit) {
        //get the ADP and SDEV data from FFCalc and update data
        List<Player> players = webScrapers.get(1).getPlayers(limit);

        if(connection == null) establishConnection();
        for(Player player: players){
            updatePlayer(player);
        }
    }

    /**
     * Add a player to the players table
     * @param player- player to be added
     */
    private void addPlayer(Player player){
        String s = "insert into "+tableName+" (ID, LastName, FirstName, Position," +
                " Team, ADP, SDEV, FullName) values ("+
                player.getRank()+", \""+
                player.getLastName()+"\", \""+
                player.getFirstName()+"\", \""+
                player.getPosition()+"\", \""+
                player.getTeam()+"\", "+
                player.getADP()+", "+
                player.getSDEV()+", \""+
                player.getName() +
                "\");";

        try {
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Update values in the players database with values from other sites
     */
    private void updatePlayer(Player player) {
        Map<String, Double> projections = player.getProjections();
        String fullName = player.getName();
        String first = player.getFirstName();
        String last = player.getLastName();
        String s = "update "+tableName+" set " +
                "Points = " +projections.get("Points")+", "+
                "RushAtt = " +projections.get("Rush Att")+", "+
                "RushYds = " +projections.get("Rush Yds")+", "+
                "RushTds = "+projections.get("Rush Tds")+", "+
                "Recs = " +projections.get("Recs")+", "+
                "RecYds = " +projections.get("Rec Yds")+", "+
                "RecTds = " +projections.get("Rec Tds")+", "+
                "PassComp = " +projections.get("Pass Cmp")+", "+
                "PassAtt = " +projections.get("Pass Att")+", "+
                "PassYds = " +projections.get("Pass Yds")+", "+
                "PassTds = " +projections.get("Pass Tds")+", "+
                "PassInts = " +projections.get("Pass Ints")+", "+
                "Fumbles = " +projections.get("Fumbles")+
                " where " +
                "FullName = \"" + fullName + "\" " +
                "or (FirstName = \""+first+"\" " +
                "and LastName = \""+last+"\");";

        try {
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Clear the database and create tables.
     */
    private void createDatabase(){
        String drop = "drop table if exists "+tableName+";";
        String s = "create table "+tableName+"(\n" +
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

    /**
     * Establish connection to sql database
     */
    private void establishConnection(){
        //this is just a local connection but it could eventually be to a server or something
        String url = "jdbc:mysql://localhost:3306/mockdraft?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close connection to sql database
     */
    private void closeConnection(){
        try {
            if(connection!=null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Clean up (in this case, close the connection)
     */
    @Override
    public void cleanUp(){
        closeConnection();
    }

}
