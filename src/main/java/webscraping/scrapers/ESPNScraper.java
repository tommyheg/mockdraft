package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public class ESPNScraper extends WebScraper{

    public ESPNScraper(ScoreType scoreType){
        switch (scoreType){
            case STANDARD: url = ""; break;
            case HALF: url = ""; break;
            case PPR: url = ""; break;
        }    }

    /**
     * Loop through the players on ESPN
     */
    @Override
    public List<Player> getPlayers(int limit) {
        return null;
    }
}
