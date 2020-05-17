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

    public SQLStorer(Site site, ScoreType scoreType, int leagueSize){
        super(site, scoreType, leagueSize);
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
        //get the list of players by web scraping
        List<Player> players = webScraper.getPlayers(limit);

        establishConnection();  //establish connection to the sql database
        createDatabase();        //first clear and create database

        for (Player player : players) {
            addPlayer(player);
        }

        createCopy();
    }

    /**
     * Add a player to the players table
     * @param player- player to be added
     */
    private void addPlayer(Player player){
        Map<String, Double> projections = player.getProjections();
        String s = "insert into players values ("+
                player.getRank()+", \""+
                player.getLastName()+"\", \""+
                player.getFirstName()+"\", \""+
                player.getPosition()+"\", \""+
                player.getTeam()+"\", "+
                projections.get("Points")+", "+
                projections.get("Rush Att")+", "+
                projections.get("Rush Yds")+", "+
                projections.get("Rush Tds")+", "+
                projections.get("Recs")+", "+
                projections.get("Rec Yds")+", "+
                projections.get("Rec Tds")+", "+
                projections.get("Pass Cmp")+", "+
                projections.get("Pass Att")+", "+
                projections.get("Pass Yds")+", "+
                projections.get("Pass Tds")+", "+
                projections.get("Pass Ints")+", "+
                projections.get("Fumbles")+", \""+
                player.getName()+
                "\");";
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
    private void createCopy(){
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
