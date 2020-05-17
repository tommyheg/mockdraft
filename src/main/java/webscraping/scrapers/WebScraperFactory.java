package webscraping.scrapers;

import pojos.ScoreType;
import webscraping.Site;

public class WebScraperFactory {

    public WebScraper getWebScraper(Site site, ScoreType scoreType){
        if(site==Site.FANTASYPROS){
            return new FantasyProsScraper(scoreType);
        } else if(site==Site.ESPN){
            return new ESPNScraper(scoreType);
        } else if(site == Site.FFCALCULATOR){
            return new FFScraper(scoreType);
        }

        return null;
    }
}
