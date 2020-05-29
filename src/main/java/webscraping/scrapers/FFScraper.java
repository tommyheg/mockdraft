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

public class FFScraper extends WebScraper {

    private String type = "standard";

    public FFScraper(ScoreType scoreType, int leagueSize) {
        if (scoreType == ScoreType.HALF) type = "half-ppr";
        else if (scoreType == ScoreType.PPR) type = "ppr";
        url = "https://fantasyfootballcalculator.com/api/v1/adp/" + type + "?teams=" + leagueSize + "&year=2020&position=all";
    }

    //get json file from ffc, parse file and fill our own json file. return a list of the players
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
        for (int i = 0; i < limit; i++) {
            //this try-catch is temporary while we have the limit
            JSONObject player;
            try {
                player = players.getJSONObject(i);
            } catch (org.json.JSONException e) {
                continue;
            }
            String name = player.getString("name");
            String position = player.getString("position");
            String team = player.getString("team");
            double adp = player.getDouble("adp");
            double sdev = player.getDouble("stdev");
            playerList.add(new Player(i + 1, name, position, team, adp, sdev));
        }
        return playerList;
    }

    //store the json file locally
    private void storeJSON(String json) {
        try {
            String filename = "./json/" + type + ".json";
            FileWriter fw = new FileWriter(filename);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
