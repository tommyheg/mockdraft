package data.storage;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;

import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;

    public SQLStorer(Site site, ScoreType scoreType){
        super(site, scoreType);
    }

    @Override
    public Player getNextPlayer(int count){
        //TODO: get the next available player
        if(connection == null) establishConnection();
        String s = "select * from players limit "+count+", 1";
        Player player = null;
        try{
            ResultSet rs = statement.executeQuery(s);
            rs.next();
            int rank = rs.getInt(1);
            String lastName = rs.getString(2);
            String firstName = rs.getString(3);
            String fullName = firstName+" "+lastName;
            String position = rs.getString(4);
            String team = rs.getString(5);
            player = new Player(rank, fullName, position, team, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return player;
    }

    @Override
    /**
     * Select one of the next X available players
     * @param- the range of players to select from
     */
    public Player getRandomPlayer(int range){
        //TODO: test this
        int random = (int) (Math.random()*range) + 1;
        System.out.println(random);
        return getNextPlayer(random);
    }

    @Override
    /**
     * Get the player from the sql if he exists, otherwise return null
     * @param- name of the player to get
     * @return the player from the database
     */
    public Player getPlayer(String name) {
        //TODO: get the player from the database with his name
        // if he doesn't exist, return null

        if(connection == null) establishConnection();
        String s = "select * from players where FullName = \""+name+"\";";
        Player player = null;
        try{
            ResultSet rs = statement.executeQuery(s);
            rs.next();
            int rank = rs.getInt(1);
            String lastName = rs.getString(2);
            String firstName = rs.getString(3);
            String fullName = firstName+" "+lastName;
            String position = rs.getString(4);
            String team = rs.getString(5);
            player = new Player(rank, fullName, position, team, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return player;
    }

    @Override
    /**
     * Remove a player from the sql database after he's been drafted
     */
    public void removePlayer(Player player){
        //TODO: remove the player from the sql

        if(connection == null) establishConnection();
        String s = "delete from players where FullName = \""+player.getName()+"\";";
        try{
            statement.executeUpdate(s);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    /**
     * Get the next X players from the sql database
     * @param limit- number of players to get
     * @return a list of the next X players
     */
    public List<Player> nextAvailablePlayers(int limit){
        //TODO: use ResultSet to add players to the list
        //if limit exceeds number of available players, add less than limit
        //unless I just keep a running list of available players in this class
        List<Player> players = new ArrayList<Player>();

        Player player = getNextPlayer(1);
        players.add(player);
        for(int i=2;i<=limit && player!=null;i++){
            player = getNextPlayer(i);
            if(player!=null) players.add(player);
        }

        return players;
    }

    @Override
    /**
     * Store the list of players from the website in an sql database
     */
    public void storeData(int limit) {
        //get the list of players by web scraping
        List<Player> players = webScraper.getPlayers(limit);

        establishConnection();  //establish connection to the sql database
        createDatabase();        //first clear and create database

        for(int i=0;i<players.size();i++){
            Player player = players.get(i);
            addPlayer(player);
        }
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

    @Override
    /**
     * Clean up all loose ends (close connection, etc)
     */
    public void cleanUp(){
        closeConnection();
    }
}
