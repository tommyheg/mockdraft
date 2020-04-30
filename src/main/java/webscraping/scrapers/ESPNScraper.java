package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public class ESPNScraper extends WebScraper{

    public ESPNScraper(ScoreType type){
        super(type);
    }

    @Override
    /**
     * Loop through the players on ESPN
     */
    public List<Player> getPlayers(int limit) {
        return null;
    }
}
