package data.storage;

import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLStorer extends DataStorer {

    private Connection connection;
    private Statement statement;
    private String query = "";

    public SQLStorer(Site site, ScoreType scoreType){
        super(site, scoreType);
    }
    
    
    @Override
    public void storeData() {
        List<Player> players = webScraper.getPlayers(5);

        establishConnection();

        for(int i=0;i<players.size();i++){
            Player player = players.get(i);
            addPlayer(player);
        }

    }

    private void addPlayer(Player player){

    }

    private void establishConnection(){
        String url = "jdbc:mysql://localhost:3306/data?useTimezone=true&serverTimezone=UTC";
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
}
