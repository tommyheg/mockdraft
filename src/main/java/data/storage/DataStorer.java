package data.storage;

import logger.Logger;
import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;
import webscraping.scrapers.WebScraperFactory;
import webscraping.scrapers.WebScraper;

import java.util.List;


public abstract class DataStorer {

    protected WebScraper webScraper;

    public DataStorer(Site site, ScoreType scoreType){
        this.webScraper = new WebScraperFactory().getWebScraper(site, scoreType);
    }

    public abstract void storeData(int limit);

    public abstract List<Player> nextAvailablePlayers(int limit);

    public abstract void removePlayer(Player player);

    public abstract Player getPlayer(String name);


}
