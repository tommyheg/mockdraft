package webscraping;

import pojos.Player;

import java.util.List;

public abstract class WebScraper {

    public abstract List<Player> getPlayers(int limit);

}
