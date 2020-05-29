package data.storage;

import pojos.ScoreType;
import webscraping.Site;
import webscraping.scrapers.WebScraperFactory;
import webscraping.scrapers.WebScraper;

import java.util.ArrayList;
import java.util.List;


public abstract class DataStorer {

    protected List<WebScraper> webScrapers;

    public DataStorer(ScoreType scoreType, int leagueSize) {
        webScrapers = new ArrayList<>();
        webScrapers.add(new WebScraperFactory().getWebScraper(Site.FFCALCULATOR, scoreType, leagueSize));
        webScrapers.add(new WebScraperFactory().getWebScraper(Site.FANTASYPROS, scoreType, leagueSize));
    }

    public abstract void storePlayers(int limit);

    public abstract void updatePlayers(int limit);

    public abstract void cleanUp();

}
