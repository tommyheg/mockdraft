package webscraping.scrapers;

import org.json.JSONObject;
import pojos.Player;
import pojos.ScoreType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class FFScraper extends WebScraper{

    public FFScraper(ScoreType scoreType, int leagueSize){
        String type = "";
        switch (scoreType){
            case STANDARD: type = "standard"; break;
            case HALF: type = "half-ppr"; break;
            case PPR: type = "ppr"; break;
        }

        url = "https://fantasyfootballcalculator.com/api/v1/adp/"+type+"?teams="+leagueSize+"&year=2020&position=all";
    }

    /**
     * This one doesn't actually webscrape. Use the
     * site's API to get a JSON file, then parse the
     * JSON file.
     */
    @Override
    public List<Player> getPlayers(int limit) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get(Response.class);
        String json = response.readEntity(String.class);
        storeJSON(json);
        System.out.println(url);
        System.out.println(json);
        return null;
    }

    /**
     * Store the json file locally so it can be
     * used later when storing probs
     * @param json- the json text
     */
    private void storeJSON(String json){
        //TODO: store the json file locally
        // this method will be used in getPlayers()
    }
}
