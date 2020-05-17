package data.storage;

import controllers.Suggestor;
import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;
import webscraping.scrapers.WebScraperFactory;
import webscraping.scrapers.WebScraper;

import java.util.List;


public abstract class DataStorer {

    protected WebScraper webScraper;

    public DataStorer(Site site, ScoreType scoreType, int leagueSize){
        this.webScraper = new WebScraperFactory().getWebScraper(site, scoreType, leagueSize);
    }

    public abstract void storeData(int limit);

    public abstract void copyData();

}
