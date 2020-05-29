package webscraping.scrapers;

import pojos.Player;

import java.util.List;

public abstract class WebScraper {

    protected String url;

    public abstract List<Player> getPlayers(int limit);

}
