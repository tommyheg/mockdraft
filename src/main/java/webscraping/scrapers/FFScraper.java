package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public class FFScraper extends WebScraper{

    public FFScraper(ScoreType scoreType, int leagueSize){
        String type = "";
        switch (scoreType){
            case STANDARD: type = ""; break;
            case HALF: type = ""; break;
            case PPR: type = ""; break;
        }

        url = "https://fantasyfootballcalculator.com/api/v1/adp/"+type+"?teams="+leagueSize+"&year=2020&position=all";
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

    /**
     * Store the json file locally so it can be
     * used later when storing probs
     * @param json- the json text
     */
    private void storeJSON(String json){
        //TODO: store the json file locally
        // this method will be used in getPlayers()
    }
}
