package webscraping.scrapers;

import org.json.JSONArray;
import org.json.JSONObject;
import pojos.Player;
import pojos.ScoreType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
public class FFScraper extends WebScraper{

    private String type;

    public FFScraper(ScoreType scoreType, int leagueSize){
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
        List<Player> playerList = new ArrayList<>();
        JSONObject root = new JSONObject(json);
        JSONArray players = root.getJSONArray("players");
        //limit can be changed to players.length eventually
        for(int i=0;i<limit;i++){
            //this try-catch is temporary while we have the limit
            JSONObject player = null;
            try{
                player = players.getJSONObject(i);
            } catch(org.json.JSONException e){
                continue;
            }
            String name = player.getString("name");
            String position = player.getString("position");
            String team = player.getString("team");
            double adp = player.getDouble("adp");
            double sdev = player.getDouble("stdev");
            playerList.add(new Player(i+1, name, position, team, adp, sdev));
         }
        return playerList;
    }

    /**
     * Store the json file locally so it can be
     * used later when storing probs
     * @param json- the json text
     */
    private void storeJSON(String json){
        try {
            String filename = "./json/"+type+".json";
            FileWriter fw = new FileWriter(filename);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
