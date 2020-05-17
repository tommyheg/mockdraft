package webscraping.scrapers;

import pojos.Player;
import pojos.ScoreType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class FFScraper extends WebScraper{

    public FFScraper(ScoreType scoreType, int leagueSize){
        String type = "";
        switch (scoreType){
            case STANDARD: type = ""; break;
            case HALF: type = ""; break;
            case PPR: type = ""; break;
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
        try {
            FileWriter fw = new FileWriter("players.json");
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
