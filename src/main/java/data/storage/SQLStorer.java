package data.storage;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;

import java.net.MalformedURLException;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;
    private String tableName;

    public SQLStorer(ScoreType scoreType, int leagueSize){

        super(scoreType, leagueSize);
//        switch(scoreType){
//            case STANDARD: tableName = "standardPlayers"; break;
//            case HALF: tableName = "halfPlayers"; break;
//            case PPR: tableName = "pprPlayers"; break;
//        }
        tableName = "players";
    }

    /**
     * Copy the full players table copy over to the players table
     */
    @Override
    public void copyData(){
        if(connection == null) establishConnection();
        try {
            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(new FileUrlResource("move_copy.sql")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
//                projections.get("Points")+", "+
//                projections.get("Rush Att")+", "+
//                projections.get("Rush Yds")+", "+
//                projections.get("Rush Tds")+", "+
//                projections.get("Recs")+", "+
//                projections.get("Rec Yds")+", "+
//                projections.get("Rec Tds")+", "+
//                projections.get("Pass Cmp")+", "+
//                projections.get("Pass Att")+", "+
//                projections.get("Pass Yds")+", "+
//                projections.get("Pass Tds")+", "+
//                projections.get("Pass Ints")+", "+
//                projections.get("Fumbles")+", "+
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
                "FullName = \"" + fullName + "\";";

        try {
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Clear the database and create tables.
     * Use create_draft.sql
     */
    private void createDatabase(){
        try {
            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(new FileUrlResource("create_draft.sql")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the copy database (only right after getting players)
     */
    public void createCopy(){
        try {
            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(new FileUrlResource("create_copy.sql")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
