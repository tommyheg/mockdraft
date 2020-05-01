package data.storage;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;

    public SQLStorer(Site site, ScoreType scoreType){
        super(site, scoreType);
    }
    
    
    @Override
    /**
     * Store the list of players from the website in an sql database
     */
    public void storeData() {
        //get the list of players by web scraping
        //limit will eventually be determined by league size and propogate down
        List<Player> players = webScraper.getPlayers(5);

        establishConnection();  //establish connection to the sql database
        createDatabase();        //first clear and create database

        for(int i=0;i<players.size();i++){
            Player player = players.get(i);
            addPlayer(player);
        }

        closeConnection();  //close connection to the sql database
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
                projections.get("Fumbles")+
                ");";
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

            connection= DriverManager.getConnection(url, user, password);

            statement=connection.createStatement();

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
