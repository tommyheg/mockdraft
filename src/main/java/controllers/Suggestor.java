package controllers;

import data.getters.DataGetter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.json.JSONArray;
import org.json.JSONObject;
import pojos.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Suggestor {

    double[][] probs;
    int players = 213, picks = 213;

    public Suggestor(){
        //TODO: parse the json file and store a 2-D array
        probs = new double[players][picks];
        for(int i=0;i<players;i++){
            for(int j = 0; j< picks; j++){
                probs[i][j]=0;
            }
        }
        fillProbs();
    }

    /**
     * Simulate the next 5 rounds for selecting this player.
     * This method will be difficult
     * @param player- player to be selected
     * @return the value of taking that player
     */
    private double simulate(Player player){
        //TODO: simulate the next 5 rounds of a player
        // We might need a list of available players
        // maybe have another dataset so we don't alter the first one
        // maybe run these in parallel with each other for each player
        return Math.random()*10+1;
    }

    /**
     * Get suggestions for the next few players.
     * Loop through the available players,
     * add the next few players at each position,
     * start a sim for each player,
     * add that player's value to the map.
     * @return the map of player suggestions
     */
    public Map<String, Double> getSuggestions(DataGetter dataGetter){
        Map<String, Double> suggestions = new HashMap<String, Double>();    //final suggestions
        List<Player> players = dataGetter.nextAvailablePlayers(100);    //next available players
        List<Player> tempPlayers = new ArrayList<Player>();                 //add the players here temporarily

        int i = 0;
        boolean complete = false;
        //loop through players until 3 rbs gotten, 3 wrs, 2 qbs, 2 tes, etc
        while(!complete){     //repeat until all those requirements met
            //stubbish for now
            Player player = players.get(i);
            if(roomForPlayer(player, tempPlayers)) {
                tempPlayers.add(player);
                complete = gottenPlayers(tempPlayers);
            }
            i++;
        }

        for(Player player: tempPlayers){
            double val = simulate(player);
            suggestions.put(player.getName(), val);
        }

        return suggestions;
    }

    /**
     * Check to see if the temporary list of players has all
     * the requirements to sim
     * @param players- temporary list of players
     * @return whether the requirements are met
     */
    private boolean gottenPlayers(List<Player> players){
        //TODO: loop through list and see if all requirements are met
        return players.size()>8;
    }

    /**
     * Check to see if there is room for the player in the
     * tempPlayers list. Check to see if the list has
     * reached the max for the player's position.
     * @param player- player to be added
     * @return if there is room for the player
     */
    private boolean roomForPlayer(Player player, List<Player> tempPlayers){
        String position = player.getPosition();
        int count = 0;
        for(Player p: tempPlayers){
            if(p.getPosition().charAt(0) == position.charAt(0)){
                count++;
                if(count==3){
                    if(position.startsWith("RB") || position.startsWith("WR")) return false;
                } else if(count == 2){
                    if(position.startsWith("QB") || position.startsWith("TE")) return false;
                }
                //TODO: include DST and K here. Not sure what we want to do with this
            }
        }
        return true;
    }

    /**
     * Fill the probs 2-D array by parsing json file
     */
    private void fillProbs(){
        //TODO: parse json file and do math
        BufferedReader br= null;
        String text = "";
        try {
            br = new BufferedReader(new FileReader("players.json"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = br.readLine();
            }
            text = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject root = new JSONObject(text);
        JSONArray players = root.getJSONArray("players");
        for(int i=0;i<players.length();i++){
            JSONObject player = players.getJSONObject(i);
            System.out.println(player);
            double adp = player.getDouble("adp");
            double sdv = player.getDouble("stdev");
            double val = 100;
            double thresh = .0001;
            for(int j = 1; j< picks && val > 0 && val < thresh; j++){
                val = algo(adp, sdv, j);
                probs[i][j] = val;
            }
        }
    }

    /**
     * Do the math here
     * @return the prob for that position
     */
    private double algo(double adp, double sdv, int pick){
        //TODO: do the math here
        NormalDistribution nd = new NormalDistribution(adp, sdv);
        return nd.cumulativeProbability(pick);
    }

}
