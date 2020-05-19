package data.storage;

import controllers.Suggestor;
import pojos.Player;
import pojos.ScoreType;
import webscraping.Site;
import webscraping.scrapers.WebScraperFactory;
import webscraping.scrapers.WebScraper;

import java.util.ArrayList;
import java.util.List;


public abstract class DataStorer {

    protected List<WebScraper> webScrapers;
    //I think doing this would be better, unless we use other scrapers
//    protected WebScraper ffc;
//    protected WebScraper pro;

    public DataStorer(ScoreType scoreType, int leagueSize){
        this.webScrapers = new ArrayList<>();
        this.webScrapers.add(new WebScraperFactory().getWebScraper(Site.FFCALCULATOR, scoreType, leagueSize));
        this.webScrapers.add(new WebScraperFactory().getWebScraper(Site.FANTASYPROS, scoreType, leagueSize));
    }

    public abstract void storeData(int limit);

    public abstract void updateData(int limit);

    public abstract void createCopy();

    public abstract void copyData();

}
