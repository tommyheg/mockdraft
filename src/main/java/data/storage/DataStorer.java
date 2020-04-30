package data.storage;

import pojos.ScoreType;
import webscraping.Site;
import webscraping.scrapers.WebScraperFactory;
import webscraping.scrapers.WebScraper;


public abstract class DataStorer {

    protected WebScraper webScraper;

    public DataStorer(Site site, ScoreType scoreType){
        this.webScraper = new WebScraperFactory().getWebScraper(site, scoreType);
    }

    public abstract void storeData();


}
