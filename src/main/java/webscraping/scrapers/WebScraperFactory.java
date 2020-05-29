package webscraping.scrapers;

import pojos.ScoreType;
import webscraping.Site;

public class WebScraperFactory {

    public WebScraper getWebScraper(Site site, ScoreType scoreType, int leagueSize) {
        switch (site) {
            case FANTASYPROS:
                return new FantasyProsScraper(scoreType);
            case FFCALCULATOR:
                return new FFScraper(scoreType, leagueSize);
            case ESPN:
                return new ESPNScraper(scoreType);
        }
        return null;
    }
}
