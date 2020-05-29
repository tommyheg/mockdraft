package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.ArrayList;
import java.util.List;

public class ESPNScraper extends WebScraper {

    public ESPNScraper(ScoreType scoreType) {
        switch (scoreType) {
            case STANDARD:
            case HALF:
            case PPR:
                url = "";
                break;
        }
    }

    @Override
    public List<Player> getPlayers(int limit) {
        return new ArrayList<Player>();
    }
}
