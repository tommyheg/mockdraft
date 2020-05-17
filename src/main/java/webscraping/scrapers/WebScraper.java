package webscraping.scrapers;

import logger.Logger;
import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public abstract class WebScraper {

    protected String url;

    public abstract List<Player> getPlayers(int limit);

}
