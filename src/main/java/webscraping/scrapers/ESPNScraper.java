package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.util.List;

public class ESPNScraper extends WebScraper{

    private final String url;

    public ESPNScraper(ScoreType type){
        if(type==ScoreType.STANDARD){
            this.url="https://www.fantasypros.com/nfl/rankings/consensus-cheatsheets.php";
        } else if(type==ScoreType.HALF){
            this.url="https://www.fantasypros.com/nfl/rankings/half-point-ppr-cheatsheets.php";
        } else if(type==ScoreType.PPR){
            this.url="https://www.fantasypros.com/nfl/rankings/ppr-cheatsheets.php";
        } else{
            throw new IllegalArgumentException("Must include scoring type");
        }
    }

    @Override
    public List<Player> getPlayers(int limit) {
        return null;
    }
}
