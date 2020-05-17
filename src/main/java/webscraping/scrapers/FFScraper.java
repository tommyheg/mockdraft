package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public class FFScraper extends WebScraper{

    public FFScraper(ScoreType scoreType){
        switch (scoreType){
            case STANDARD: url = ""; break;
            case HALF: url = ""; break;
            case PPR: url = ""; break;
        }
    }

    /**
     * This one doesn't actually webscrape. Use the
     * site's API to get a JSON file, then parse the
     * JSON file.
     */
    @Override
    public List<Player> getPlayers(int limit) {
        return null;
    }
}
