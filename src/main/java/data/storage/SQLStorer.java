package data.storage;

import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;

import java.sql.Connection;
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
        this.connection = establishConnection();


    }

    private Connection establishConnection(){
        return null;
    }
}
